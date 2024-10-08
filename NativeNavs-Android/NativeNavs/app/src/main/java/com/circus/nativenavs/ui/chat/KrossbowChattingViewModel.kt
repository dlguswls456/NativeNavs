package com.circus.nativenavs.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.circus.nativenavs.config.ApplicationClass
import com.circus.nativenavs.data.ChatRoomDto
import com.circus.nativenavs.data.ChatUserCountDto
import com.circus.nativenavs.data.MessageDto
import com.circus.nativenavs.data.RequestTranslate
import com.circus.nativenavs.data.service.ChatService
import com.circus.nativenavs.data.service.TranslateService
import com.circus.nativenavs.util.SharedPref
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.convertAndSend
import org.hildan.krossbow.stomp.conversions.moshi.withMoshi
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "KrossbowChattingViewMod"

class KrossbowChattingViewModel : ViewModel() {

    private val _chatRoomList = MutableLiveData<List<ChatRoomDto>>()
    val chatRoomList: LiveData<List<ChatRoomDto>> = _chatRoomList

    private val _currentChatRoom = MutableLiveData<ChatRoomDto>()
    val currentChatRoom: LiveData<ChatRoomDto> = _currentChatRoom

    private val _chatRoomId = MutableLiveData(-1)
    val chatRoomId: LiveData<Int> = _chatRoomId

    private val _chatMessages = MutableLiveData<List<MessageDto>>()
    val chatMessages: LiveData<List<MessageDto>> = _chatMessages

    private val chatRetrofit = ApplicationClass.retrofit.create(ChatService::class.java)

    private val translateRetrofit =
        ApplicationClass.translationRetrofit.create(TranslateService::class.java)

    private val _uiState = MutableLiveData(ChatScreenUiState())
    val uiState: LiveData<ChatScreenUiState> = _uiState

    private lateinit var stompSession: StompSession
    private var isConnected = false

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun translateMessage(message: RequestTranslate, position: Int) {
        _uiState.value?.let {
            viewModelScope.launch {
                try {
                    val result = translateRetrofit.getTranslatedMessage(message)
                    val returnMessage = result.message.result.translatedText

                    it.messages[position].translatedContent = returnMessage
                    it.messages[position].isTranslated = true

                    _uiState.postValue(it.copy(messages = it.messages))

                } catch (e: Exception) {
                    Log.d(TAG, "translateMessage: ${e.message}")
                }
            }
        }
    }

    fun translateMessage(position: Int) {
        _uiState.value?.let {
            it.messages[position].isTranslated = !it.messages[position].isTranslated
            _uiState.postValue(it.copy(messages = it.messages))
        }
    }

    fun createChatRoom(tourId: Int) {
        viewModelScope.launch {
            _currentChatRoom.value = chatRetrofit.createChatRoom(tourId)
            currentChatRoom.value?.let {
                _chatRoomId.value = it.roomId
            }
        }
    }

    fun setCurrentChatRoom(chatRoom: ChatRoomDto) {
        _currentChatRoom.value = chatRoom
        currentChatRoom.value?.let {
            _chatRoomId.value = it.roomId
        }
    }

    fun setCurrentChatRoom(roomId: Int) {
        viewModelScope.launch {
            _currentChatRoom.value = chatRetrofit.getChatRoom(roomId)
            currentChatRoom.value?.let {
                _chatRoomId.value = roomId
            }
        }
    }

    fun getChatRoomList() {
        viewModelScope.launch {
            _chatRoomList.value = chatRetrofit.getChatRoomList()
        }
    }

    fun setChatRoomId(roomId: Int) {
        _chatRoomId.value = roomId
    }

    fun getChatMessages(roomId: Int) {
        viewModelScope.launch {
            _chatMessages.value = chatRetrofit.getChatMessages(roomId)
            _chatMessages.value?.let {
                setMessages(it)
            }

        }
    }

    fun setMessage(content: String) {
        _uiState.value?.let {
            it.message = content
        }
    }

    private fun setMessages(list: List<MessageDto>) {
        _uiState.value?.let { currentState ->
            _uiState.postValue(currentState.copy(messages = list))
        }
    }

    fun setSenderInfo(senderId: Int, senderNickname: String, senderImg: String) {
        _uiState.value?.let {
            it.senderId = senderId
            it.senderNickName = senderNickname
            it.senderImg = senderImg
        }
    }

    fun resetUiState() {
        _uiState.postValue(ChatScreenUiState())
    }

    fun connectWebSocket() {
        viewModelScope.launch {
            try {
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    .callTimeout(Duration.ofMinutes(1))
                    .pingInterval(Duration.ofSeconds(10))
                    .build()

                val wsClient = OkHttpWebSocketClient(okHttpClient)
                val stompClient = StompClient(wsClient)
                stompSession = stompClient.connect(
                    url = "ws://i11d110.p.ssafy.io/api/ws-stomp/websocket",
                    customStompConnectHeaders = mapOf(
                        "Authorization" to "${SharedPref.accessToken}",
                        "roomId" to "${chatRoomId.value}"
                    ),
                ).withMoshi(moshi)
                updateConnectionStatus(ConnectionStatus.CONNECTING)

                observeMessages()
                updateConnectionStatus(ConnectionStatus.OPENED)
            } catch (e: Exception) {
                Log.e(TAG, "WebSocket connection failed: ", e)
                updateConnectionStatus(ConnectionStatus.FAILED)
            }
        }
    }

    private fun updateConnectionStatus(connectionStatus: ConnectionStatus) {
        _uiState.postValue(_uiState.value?.copy(connectionStatus = connectionStatus))
    }

    private suspend fun observeMessages() {
        try {
            val subscriptionMessage = stompSession.subscribe(
                StompSubscribeHeaders(destination = "/room/${chatRoomId.value}")
            )

            val subscriptionConnection = stompSession.subscribe(
                StompSubscribeHeaders(destination = "/status/room/${chatRoomId.value}")
            )

            isConnected = true

            subscriptionMessage.collect { frame ->
                val newMessage = moshi.adapter(MessageDto::class.java).fromJson(frame.bodyAsText)
                newMessage?.let {
                    handleOnMessageReceived(newMessage)
                }
            }

            subscriptionConnection.collect { frame ->
                val userCount =
                    moshi.adapter(ChatUserCountDto::class.java).fromJson(frame.bodyAsText)
                userCount?.let {
                    if (userCount.userCount == 2) {
                        markMessagesAsRead()
                    }
                }
            }

        } catch (e: Exception) {
            isConnected = false
            Log.e(TAG, "Message observation failed: ", e)
        }
    }

    private fun markMessagesAsRead() {
        val messages = uiState.value?.let {
            it.messages.toMutableList().map {
                it.copy(messageChecked = true)
            }
        }
        _uiState.value = messages?.let { _uiState.value?.copy(messages = it) }
    }


    private fun handleOnMessageReceived(message: MessageDto) {
        try {
            addMessage(message)
        } catch (e: Exception) {
            Log.e(TAG, "handleOnMessageReceived: ", e)
        }
    }

    private fun addMessage(message: MessageDto) {
        if (message.senderId != SharedPref.userId) {
            markMessagesAsRead()
        }

        val messages = uiState.value?.messages?.toMutableList()
        messages?.add(message)
        _uiState.value = messages?.let { _uiState.value?.copy(messages = it) }
    }

    fun sendMessage(messageSent: () -> Unit) {
        val message = createMessage()
        if (message.content.isEmpty()) return

        viewModelScope.launch {
            try {
                stompSession.withMoshi(moshi).convertAndSend(
                    StompSendHeaders(destination = "/send/${chatRoomId.value}"),
                    message
                )

                messageSent()
                clearMessage()
            } catch (e: Exception) {
                Log.e(TAG, "Message sending failed: ", e)
            }
        }
    }

    private fun createMessage(): MessageDto {
        val tempMessage = _uiState.value?.let {
            MessageDto(
                roomId = currentChatRoom.value!!.roomId,
                senderId = it.senderId,
                senderNickname = it.senderNickName,
                senderProfileImage = it.senderImg,
                content = it.message,
                sendTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    .toString(),
                messageChecked = false
            )
        } ?: MessageDto()
        return tempMessage
    }

    private fun clearMessage() {
        viewModelScope.launch {
            delay(50)
            _uiState.postValue(_uiState.value?.copy(message = ""))
        }
    }

    fun disconnectWebSocket() {
        if (isConnected) {
            viewModelScope.launch {
                stompSession.disconnect()
                updateConnectionStatus(ConnectionStatus.CLOSED)
                isConnected = false
            }
        }

    }
}