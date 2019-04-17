package com.labs64.netlicensing.exception

import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Netlicensing
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response

/**
 * ServiceException is thrown when error response is received from the service.
 */
class ServiceException
/**
 * Construct a `ServiceException` with the service error response object.
 *
 * @param status
 * response status
 * @param headers
 * response headers
 * @param errorResponse
 * the service response containing the error info.
 */
    (val status: Response.Status?, headers: MultivaluedMap<String, Any>?, val errorResponse: Netlicensing?) :
    NetLicensingException(
        SchemaFunction.infosToMessage(errorResponse)
    ) {
    val headers: MultivaluedMap<String, Any>?

    init {
        this.headers = headers
    }

    companion object {

        private val serialVersionUID = 5253993578845477398L
    }
}
