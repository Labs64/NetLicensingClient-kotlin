package com.labs64.netlicensing.provider

import com.labs64.netlicensing.exception.RestException
import com.labs64.netlicensing.provider.auth.Authentication
import org.glassfish.jersey.client.ClientConfig
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature
import javax.ws.rs.ProcessingException
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder.newClient
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.NoContentException
import javax.ws.rs.core.Response

/**
 * Low level REST client implementation.
 * <p>
 * This will also log each request in INFO level.
 */
class RestProviderJersey(private val basePath: String) : AbstractRestProvider() {

    private val DEFAULT_ACCEPT_TYPES = arrayOf<MediaType>(MediaType.APPLICATION_XML_TYPE)

    private var client: Client? = null

    init {
        configure(JerseyDefaultConfig())
    }

    override fun <REQ, RES> call(
        method: String,
        urlTemplate: String,
        request: REQ,
        responseType: Class<RES>,
        queryParams: Map<String, Any?>?
    ): RestResponse<RES> {
        var target = getTarget(basePath)
        addAuthHeaders(target, authentication)
        target = target.path(urlTemplate)
        if (queryParams != null && queryParams.isNotEmpty()) {
            for (paramKey in queryParams.keys) {
                target = target.queryParam(paramKey, queryParams[paramKey])
            }
        }

        val response: Response
        val builder = target.request(*DEFAULT_ACCEPT_TYPES).header(
            HttpHeaders.USER_AGENT,
            configuration!!.userAgent
        )
        if ("POST" == method || "PUT" == method) {
            val requestEntity = Entity.entity(request, MediaType.APPLICATION_FORM_URLENCODED_TYPE)
            response = builder.method(method, requestEntity)
        } else {
            response = builder.method(method)
        }

        val restResponse = RestResponse<RES>()
        restResponse.statusCode = response.status
        restResponse.headers = response.headers
        restResponse.entity = readEntity<RES>(response, responseType)
        return restResponse
    }

    private inner class JerseyDefaultConfig : RestProvider.Configuration {
        override val userAgent: String
            get() = "NetLicensing/Java " + System.getProperty("java.version") + " (http://netlicensing.io)"

        override val isLoggingEnabled: Boolean
            get() = true
    }

    /**
     * Get static instance of RESTful client
     *
     * @return RESTful client
     */
    private fun getClient(configuration: RestProvider.Configuration?): Client? {
        // initialize client only once since it's expensive operation
        if (client == null) {
            synchronized(RestProviderJersey::class.java) {
                if (client == null) {
                    client = newClient(ClientConfig())
                }
            }
        }
        return client
    }

    /**
     * Get the RESTful client target
     *
     * @param basePath
     * base provider path
     * @return RESTful client target
     */
    private fun getTarget(basePath: String): WebTarget {
        return getClient(configuration)!!.target(basePath)
    }

    /**
     * @param target
     * target object, to which the authentication headers will be added
     * @param auth
     * an object providing the authentication info
     */
    private fun addAuthHeaders(target: WebTarget, auth: Authentication?) {
        auth?.let {
            // see also https://jersey.java.net/documentation/latest/client.html, chapter "Securing a Client"
            target.register(HttpAuthenticationFeature.basic(auth.username, auth.password))
        }
    }

    @Throws(RestException::class)
    private fun <RES> readEntity(response: Response, responseType: Class<RES>): RES? {
        var buffered = false
        try {
            buffered = response.bufferEntity()
            return response.readEntity(responseType)
        } catch (ex: ProcessingException) {
            if (response.status == Response.Status.NO_CONTENT.statusCode || ex.cause is NoContentException) {
                return null
            } else {
                if ((response.statusInfo.family == Response.Status.Family.CLIENT_ERROR) || (response.statusInfo.family == Response.Status.Family.SERVER_ERROR)) {
                    return null // Ignore content interpretation errors if status is an error already
                }

                var body = ""
                if (buffered) {
                    body = " '" + response.readEntity(String::class.java) + "' of type '" + response.mediaType + "'"
                }
                throw RestException("Could not interpret the response body $body")
            }
        }
    }
}