package com.labs64.netlicensing.provider.auth

/**
 * Generic interface for creating authentication headers.
 */
interface Authentication {

    val username: String

    val password: String
}