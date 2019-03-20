package com.labs64.netlicensing.provider

import com.labs64.netlicensing.exception.RestException
import com.labs64.netlicensing.provider.auth.Authentication

interface RestProvider {

    interface Configuration {

        val userAgent: String

        val isLoggingEnabled: Boolean

    }

    /**
     * Helper method for performing REST requests with optional REST parameter map.
     *
     *
     * This method has a long list of parameters. It is only intended for internal use.
     *
     * @param method
     * the HTTP method to be used, i.e. GET, PUT, POST.
     * @param urlTemplate
     * the REST URL urlTemplate.
     * @param request
     * optional: The request body to be sent to the server. May be null.
     * @param responseType
     * optional: expected response type. In case no responseType body is expected, responseType may be null.
     * @param queryParams
     * optional: The REST query parameters values. May be null.
     * @param <REQ>
     * type of the request entity
     * @param <RES>
     * type of the responseType entity
     * @return the responseType entity received from the server, or null if responseType is null.
    </RES></REQ> */
    @Throws(RestException::class)
    fun <REQ, RES> call(
        method: String, urlTemplate: String, request: REQ, responseType: Class<RES>,
        queryParams: Map<String, Any>
    ): RestResponse<RES>

    /**
     * @param username
     * username used for authentication
     * @param password
     * password used for authentication
     * @return authenticated RESTful provider
     */
    fun authenticate(username: String, password: String): RestProvider

    /**
     * @param token
     * token used for authentication
     * @return authenticated RESTful provider
     */
    fun authenticate(token: String): RestProvider

    /**
     * @param authentication
     * [Authentication] object
     * @return authenticated RESTful provider
     */
    fun authenticate(authentication: Authentication): RestProvider

    /**
     * @param configuration
     * [Configuration] configuration to use for the provider
     */
    fun configure(configuration: Configuration)

}