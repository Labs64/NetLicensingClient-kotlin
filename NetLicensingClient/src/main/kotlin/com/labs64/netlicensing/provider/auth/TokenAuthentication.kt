package com.labs64.netlicensing.provider.auth

class TokenAuthentication

    (override val password: String) : Authentication {

    override val username: String
        get() = "apiKey"
}