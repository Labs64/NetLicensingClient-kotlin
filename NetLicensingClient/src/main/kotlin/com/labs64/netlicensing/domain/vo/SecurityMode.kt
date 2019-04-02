package com.labs64.netlicensing.domain.vo

/**
 * Enumerates possible security modes for accessing the NetLicensing API.
 *
 *
 * See [https://www.labs64.de/confluence/x/pwCo] for details.
 *
 */
enum class SecurityMode {
    BASIC_AUTHENTICATION, APIKEY_IDENTIFICATION, ANONYMOUS_IDENTIFICATION
}