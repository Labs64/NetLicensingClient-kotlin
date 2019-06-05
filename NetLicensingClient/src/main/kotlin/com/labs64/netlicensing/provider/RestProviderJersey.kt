package com.labs64.netlicensing.provider

import com.labs64.netlicensing.exception.RestException
import com.labs64.netlicensing.provider.auth.Authentication
import com.labs64.netlicensing.util.PackageUtils
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
            get() = "NetLicensing/Kotlin " + PackageUtils.getImplementationVersion() + "/" + KotlinVersion.CURRENT +
                    " (http://netlicensing.io)"

        override val isLoggingEnabled: Boolean
            get() = true
    }

    private fun getClient(): Client? {
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

    private fun getTarget(basePath: String): WebTarget {
        return getClient()!!.target(basePath)
    }

    private fun addAuthHeaders(target: WebTarget, auth: Authentication?) {
        auth?.let {
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