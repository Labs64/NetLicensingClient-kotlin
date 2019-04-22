package com.labs64.netlicensing.provider

import com.labs64.netlicensing.exception.RestException
import com.labs64.netlicensing.provider.auth.Authentication

interface RestProvider {

    interface Configuration {

        val userAgent: String

        val isLoggingEnabled: Boolean
    }

    @Throws(RestException::class)
    fun <REQ, RES> call(
        method: String,
        urlTemplate: String,
        request: REQ,
        responseType: Class<RES>,
        queryParams: Map<String, Any?>?
    ): RestResponse<RES>

    fun authenticate(username: String, password: String): RestProvider

    fun authenticate(token: String): RestProvider

    fun authenticate(authentication: Authentication): RestProvider

    fun configure(configuration: Configuration)
}