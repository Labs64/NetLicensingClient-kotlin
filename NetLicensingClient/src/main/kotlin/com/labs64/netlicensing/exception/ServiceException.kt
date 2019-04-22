package com.labs64.netlicensing.exception

import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Netlicensing
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response

class ServiceException
    (val status: Response.Status?, headers: MultivaluedMap<String, Any>?, val errorResponse: Netlicensing?) :
    NetLicensingException(
        SchemaFunction.infosToMessage(errorResponse)
    ) {
    val headers: MultivaluedMap<String, Any>?

    init {
        this.headers = headers
    }
}
