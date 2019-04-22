package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.EntityFactory
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.MetaInfo
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.domain.vo.SecurityMode
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.exception.RestException
import com.labs64.netlicensing.exception.ServiceException
import com.labs64.netlicensing.provider.RestProvider
import com.labs64.netlicensing.provider.RestProviderJersey
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Netlicensing
import com.labs64.netlicensing.util.CheckUtils
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

    @Throws(NetLicensingException::class)
    internal operator fun <RES> get(
        context: Context,
        urlTemplate: String,
        queryParams: MutableMap<String, Any?>,
        resultType: Class<RES>,
        vararg meta: MetaInfo
    ): RES {
        val netlicensing = request(context, HttpMethod.GET, urlTemplate, null, queryParams)
        if (meta.size > 0 && netlicensing!!.id != null) {
            meta[0].setValue(Constants.PROP_ID, netlicensing.id)
        }
        return entityFactory.create(netlicensing!!, resultType)
    }

    @Throws(NetLicensingException::class)
    internal fun delete(
        context: Context,
        urlTemplate: String,
        queryParams: MutableMap<String, Any?>?
    ) {
        request(context, HttpMethod.DELETE, urlTemplate, null, queryParams)
    }

    @Throws(NetLicensingException::class)
    internal fun <RES> post(
        context: Context,
        urlTemplate: String,
        request: Form,
        resultType: Class<RES>,
        vararg meta: MetaInfo
    ): RES? {
        val netlicensing = request(context, HttpMethod.POST, urlTemplate, request, null)
        // if response has no content
        if (netlicensing == null) {
            return null
        } else {
            if (meta.size > 0 && netlicensing.id != null) {
                meta[0].setValue(Constants.PROP_ID, netlicensing.id)
            }
            return entityFactory.create(netlicensing, resultType)
        }
    }

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

    @Throws(NetLicensingException::class)
    fun request(
        context: Context,
        method: String,
        urlTemplate: String,
        request: Form?,
        queryParams: MutableMap<String, Any?>?
    ): Netlicensing? {
        CheckUtils.paramNotNull(context, "context")

        var combinedRequest = request
        var combinedQueryParams: MutableMap<String, Any?>? = queryParams
        if (StringUtils.isNotBlank(context.getVendorNumber())) {
            if (HttpMethod.POST == method) {
                if (combinedRequest == null) {
                    combinedRequest = Form()
                }
                combinedRequest.param(Constants.Vendor.VENDOR_NUMBER, context.getVendorNumber())
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
            method, urlTemplate, combinedRequest, Netlicensing::class.java,
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

    @Throws(RestException::class)
    private fun configure(
        restProvider: RestProvider,
        context: Context
    ) {
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

    private fun isErrorStatus(status: Response.Status): Boolean {
        return status.family == Response.Status.Family.CLIENT_ERROR || status.family == Response.Status.Family.SERVER_ERROR
    }
}