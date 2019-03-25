package com.labs64.netlicensing.provider.auth

class TokenAuthentication
/**
 * Token auth constructor.
 *
 * @param token
 * authentication token
 */
    (override val password: String) : Authentication {

    override val username: String
        get() = "apiKey"

}