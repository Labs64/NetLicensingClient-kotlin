package com.labs64.netlicensing.provider.auth

class UsernamePasswordAuthentication
/**
 * Constructor
 *
 * @param username
 * for basic HTTP authentication
 * @param password
 * for basic HTTP authentication
 */
    (override val username: String, override val password: String) : Authentication
