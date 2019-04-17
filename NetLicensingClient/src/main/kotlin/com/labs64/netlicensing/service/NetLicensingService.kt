package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.EntityFactory
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.domain.vo.SecurityMode
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.exception.RestException
import com.labs64.netlicensing.exception.ServiceException
import com.labs64.netlicensing.provider.RestProvider
import com.labs64.netlicensing.provider.RestProviderJersey
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Netlicensing
import org.apache.commons.lang3.StringUtils
import javax.ws.rs.HttpMethod
import javax.ws.rs.core.Form
import javax.ws.rs.core.Response

object NetLicensingService {

    private var instance: NetLicensingService? = null

    private val entityFactory = EntityFactory()

    /**
     * @return instance of NetLicensingService class
     */
    internal fun getInstance(): NetLicensingService? {
        if (instance == null) {
            synchronized(NetLicensingService::class.java) {
                if (instance == null) {
                    instance = NetLicensingService
                }
            }
        }
        return instance
    }

    /**
     * Helper method for performing GET request to NetLicensing API service that returns page of items with type
     * resultType.
     *
     * @param context
     * context for the NetLicensing API call
     * @param urlTemplate
     * the REST URL template
     * @param queryParams
     * The REST query parameters values. May be null if there are no parameters.
     * @param resultType
     * the type of the item of the result page
     * @return page of items with type resultType from the response
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     */
    @Throws(NetLicensingException::class)
    internal fun <RES : Any> list(
        context: Context,
        urlTemplate: String,
        queryParams: MutableMap<String, Any?>,
        resultType: Class<RES>
    ): Page<RES> {
        val netlicensing = request(context, HttpMethod.GET, urlTemplate, null, queryParams)
        return entityFactory.createPage(netlicensing, resultType)
    }

    /**
     * Helper method for performing request to NetLicensing API services. Knows about context for the NetLicensing API
     * calls, does authentication, provides error handling based on status of the response.
     *
     * @param context
     * context for the NetLicensing API call
     * @param method
     * the HTTP method to be used, i.e. GET, POST, DELETE
     * @param urlTemplate
     * the REST URL template
     * @param request
     * The request body to be sent to the server. May be null.
     * @param queryParams
     * The REST query parameters values. May be null if there are no parameters.
     * @return [Netlicensing] response object
     * @throws NetLicensingException
     */
    @Throws(NetLicensingException::class)
    fun request(
        context: Context,
        method: String,
        urlTemplate: String,
        request: Form?,
        queryParams: MutableMap<String, Any?>?
    ): Netlicensing? {
        // CheckUtils.paramNotNull(context, "context")// TODO(AY): uncomment

        var combinedRequest = request
        var combinedQueryParams: MutableMap<String, Any?>? = queryParams
        if (StringUtils.isNotBlank(context.getVendorNumber())) {
            if (HttpMethod.POST == method) {
                if (combinedRequest == null) {
                    combinedRequest = Form()
                }
                combinedRequest!!.param(Constants.Vendor.VENDOR_NUMBER, context.getVendorNumber())
            } else {
                if (combinedQueryParams == null) {
                    combinedQueryParams = HashMap()
                }
                combinedQueryParams[Constants.Vendor.VENDOR_NUMBER] = context.getVendorNumber()
            }
        }

        val restProvider = RestProviderJersey(context.getBaseUrl()!!)
        configure(restProvider, context)

        val response = restProvider.call(
            method, urlTemplate, combinedRequest, Netlicensing::class.java!!,
            combinedQueryParams
        )

        val status = Response.Status.fromStatusCode(response.statusCode)
        if (!isErrorStatus(status)) {
            when (status) {
                Response.Status.OK -> return response.entity
                Response.Status.NO_CONTENT -> return null
                else -> throw RestException(
                    String.format(
                        "Unsupported response status code %s: %s",
                        status.getStatusCode(), status.getReasonPhrase()
                    )
                )
            }
        } else {
            if (SchemaFunction.hasErrorInfos(response.entity)) {
                throw ServiceException(status, response.headers, response.entity)
            } else {
                throw RestException(
                    String.format(
                        "Unknown service error %s: %s", status.getStatusCode(),
                        status.getReasonPhrase()
                    )
                )
            }
        }
    }

    /**
     * Passes the authentication data specified in the context of the call to the RESTful provider.
     *
     * @param restProvider
     * RESTful provider to be authenticated
     * @param context
     * additional context
     * @throws RestException
     */
    @Throws(RestException::class)
    private fun configure(restProvider: RestProvider, context: Context) {
        if (context.getSecurityMode() == null) {
            throw RestException("Security mode must be specified")
        }
        when (context.getSecurityMode()) {
            SecurityMode.BASIC_AUTHENTICATION -> restProvider.authenticate(
                context.getUsername()!!,
                context.getPassword()!!
            )
            SecurityMode.APIKEY_IDENTIFICATION -> restProvider.authenticate(context.getApiKey()!!)
            SecurityMode.ANONYMOUS_IDENTIFICATION -> {
            }
            else -> throw RestException("Unknown security mode")
        }
        if (context.containsKey(RestProvider.Configuration::class.java)) {
            val config = context.getObject(RestProvider.Configuration::class.java)
            if (config is RestProvider.Configuration) {
                restProvider.configure((config as RestProvider.Configuration?)!!)
            }
        }
    }

    /**
     * @param status
     * info about status
     * @return true if HTTP status represents client error or server error, false otherwise
     */
    private fun isErrorStatus(status: Response.Status): Boolean {
        return status.family == Response.Status.Family.CLIENT_ERROR || status.family == Response.Status.Family.SERVER_ERROR
    }
}