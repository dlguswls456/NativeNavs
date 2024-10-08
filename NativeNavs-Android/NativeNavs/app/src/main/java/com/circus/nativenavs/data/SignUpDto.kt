package com.circus.nativenavs.data

data class SignUpDto(
    val email: String,
    val password: String,
    val isNav: Boolean,
    val nickname: String,
    val userLanguage: String,
    val name: String,
    val phone: String,
    val nation: String,
    val birth : String,
    val isKorean : Boolean,
    val device : String = "",
    val image : String = ""
)

data class LanguageListDto (val language : List<String>)