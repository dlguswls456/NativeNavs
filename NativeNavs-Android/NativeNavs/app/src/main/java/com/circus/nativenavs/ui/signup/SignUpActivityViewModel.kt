package com.circus.nativenavs.ui.signup

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.circus.nativenavs.config.ApplicationClass
import com.circus.nativenavs.data.LanguageDto
import com.circus.nativenavs.data.LanguageListDto
import com.circus.nativenavs.data.LanguageServerDto
import com.circus.nativenavs.data.SignUpDto
import com.circus.nativenavs.data.service.UserService
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private const val TAG = "SignUpActivityViewModel"
class SignUpActivityViewModel : ViewModel() {

    private val _signUpDTO = MutableLiveData(
        SignUpDto(
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            "",
            "",
            false
        )
    )
    val signUpDTO: LiveData<SignUpDto> = _signUpDTO

    private val _languageList = MutableLiveData(LanguageListDto(emptyList()))
    val languageList: LiveData<LanguageListDto> = _languageList

    private val _nicknameCheck = MutableLiveData<Boolean>(false)
    val nicknameCheck : LiveData<Boolean> = _nicknameCheck

    private val _emailStatusCode = MutableLiveData<Int>()
    val emailStatusCode: LiveData<Int> get() = _emailStatusCode

    private val _emailStatus = MutableLiveData<Int>()
    val emailStatus: LiveData<Int> get() = _emailStatus

    private val _signStatus = MutableLiveData<Int>()
    val signStatus: LiveData<Int> get() = _signStatus

    private val _languageServerList = MutableLiveData(LanguageServerDto())
    val languageServerList : LiveData<LanguageServerDto> get() = _languageServerList

    private val _languageCheckList = MutableLiveData<List<LanguageDto>>()
    val languageCheckList : LiveData<List<LanguageDto>> get() = _languageCheckList

    private val _checkCount = MutableLiveData<Int>(0)
    val checkCount : LiveData<Int> get() = _checkCount

    private var _body = MutableLiveData<MultipartBody.Part?>(null)
    val body : LiveData<MultipartBody.Part?> get() = _body
    private var _imageUri = MutableLiveData<Uri>()
    val imageUri : LiveData<Uri> = _imageUri
    fun updateImageFile(image :MultipartBody.Part, uri : Uri){
        _imageUri.value = uri
        _body.value = image
    }

    fun updateCheckList(language : String, isChecked: Boolean){
        var count = 0
        _languageCheckList.value?.onEach {
            if(it.language == language) {
                it.isChecked = isChecked
            }
            if(isChecked) count++
        }
        _checkCount.value = count
    }

    fun updateLanguageList(){
        viewModelScope.launch {
            _languageServerList.value = retrofit.getLanguageList()
            _languageCheckList.value = languageServerList.value?.map { LanguageDto(it.name, isChecked = false) }
        }
    }

    private val retrofit = ApplicationClass.retrofit.create(UserService::class.java)

    private val _dupliState = MutableLiveData<Pair<Int,String>>()
    val dupliState: LiveData<Pair<Int,String>> get() = _dupliState

    fun isDupli(nickname:String){
       viewModelScope.launch {
           val response = retrofit.isDupliNick(nickname)
           _dupliState.postValue(Pair(response.code(),response.body().toString()))
       }
    }

    fun signUp() {
        viewModelScope.launch {
            if (_body.value != null ){
                val userJson = Gson().toJson(_signUpDTO.value)
                val userRequestBody = userJson.toRequestBody("application/json".toMediaTypeOrNull())
                val requestBody = MultipartBody.Part.createFormData("user", null, userRequestBody)
                val response = retrofit.postSignUp(requestBody, _body.value!!)
                _signStatus.postValue(response?.code())
            }
            else _signStatus.postValue(999)
        }
    }

    fun getEmailCode(email : String){
        viewModelScope.launch {
            val response = retrofit.getEmailVerifyCode(email)
            _emailStatus.postValue(response.code())

        }
    }

    fun setEmailCode(email: String, code: String){
        viewModelScope.launch {
            val response = retrofit.setEmailVerifyCode(email,code)

            _emailStatusCode.postValue(response.code())
        }
    }

    fun updateLanguage(language : List<String>){
        _languageList.value = LanguageListDto(language)
    }

    fun updateNicknameCheck(isChecked : Boolean){
        _nicknameCheck.value = isChecked
    }

    fun updateEmail(email: String) {
        _signUpDTO.value = _signUpDTO.value?.copy(email = email)
    }

    fun updatePassword(password: String) {
        _signUpDTO.value = _signUpDTO.value?.copy(password = password)
    }

    fun updateNickname(nickname: String) {
        _signUpDTO.value = _signUpDTO.value?.copy(nickname = nickname)
    }

    fun updateIsNav(isNav: Boolean) {
        _signUpDTO.value = _signUpDTO.value?.copy(isNav = isNav)
    }

    fun updateName(name: String) {
        _signUpDTO.value = _signUpDTO.value?.copy(name = name)
    }

    fun updatePhone(phone: String) {
        _signUpDTO.value = _signUpDTO.value?.copy(phone = phone)
    }

    fun updateIsKorean(isKorean: Boolean) {
        _signUpDTO.value = _signUpDTO.value?.copy(isKorean = isKorean)
    }

    fun updateBirth(birth: String) {
        _signUpDTO.value = _signUpDTO.value?.copy(birth = birth)
    }

    fun updateNation(nation: String) {
        _signUpDTO.value = _signUpDTO.value?.copy(nation = nation)
    }

    fun updateUserLanguage(language: String){
        _signUpDTO.value = _signUpDTO.value?.copy(userLanguage = language)
    }

    @Override
    override fun toString(): String {
        return "이메일 : " + _signUpDTO.value?.email +
                "\n 비밀번호 : " + _signUpDTO.value?.password +
                "\n 유저타입 : " + _signUpDTO.value?.isNav +
                "\n 닉네임 : " + _signUpDTO.value?.nickname +
                "\n 구사가능언어 : " + _signUpDTO.value?.userLanguage +
                "\n 이름 : " + _signUpDTO.value?.name +
                "\n 휴대폰 : " + _signUpDTO.value?.phone +
                "\n 국가 : " + _signUpDTO.value?.nation +
                "\n 생년월일 : " + _signUpDTO.value?.birth +
                "\n 앱 설정 언어 : " + _signUpDTO.value?.isKorean
    }

}
