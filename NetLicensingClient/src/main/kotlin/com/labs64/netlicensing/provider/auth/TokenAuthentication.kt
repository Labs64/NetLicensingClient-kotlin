package com.labs64.netlicensing.provider.auth

class TokenAuthentication
/**
 * Token auth constructor.
 *
 * @param token
 * authentication token
 */
    (val password: String) : Authentication {

    val username: String
        get() = "apiKey"

}