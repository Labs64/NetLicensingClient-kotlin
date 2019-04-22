package com.labs64.netlicensing.provider

import javax.ws.rs.core.MultivaluedMap

class RestResponse<T> {

    var statusCode: Int = 0

    var headers: MultivaluedMap<String, Any>? = null

    var entity: T? = null
}
