package com.blackcows.butakaeyak.ui.SignIn

object SignUpValidation {
    // 010-xxxx-xxxx
    fun isValidId(id: String): Boolean {
        val idPattern = "^([a-zA-Z0-9]+$)".toRegex()
        return idPattern.matches(id)
    }

    // 한국어만 가능
    fun isValidName(name: String) : Boolean {
        val namePattern = "^([가-힣]+$)".toRegex()
        return namePattern.matches(name)
    }

    // 0보다 큰 숫자만 가능
    fun isValidAge(age: Int) : Boolean {
        return age > 0
    }

    // 영어, .!@#$만 가능
    fun isValidPw(pw : String) : Boolean {
        val pwPattern = "^([a-zA-Z0-9.!@#$]+$)".toRegex()
        return pwPattern.matches(pw)
    }
}