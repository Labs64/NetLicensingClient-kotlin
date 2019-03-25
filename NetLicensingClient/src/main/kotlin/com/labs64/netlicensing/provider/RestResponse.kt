package com.labs64.netlicensing.provider

import javax.ws.rs.core.MultivaluedMap

/**
 * Contains info about response together with response entity.
 *
 * @param <T>
 * type of response entity
</T> */
class RestResponse<T> {

    var statusCode: Int = 0

    var headers: MultivaluedMap<String, Any>? = null

    var entity: T? = null

}
