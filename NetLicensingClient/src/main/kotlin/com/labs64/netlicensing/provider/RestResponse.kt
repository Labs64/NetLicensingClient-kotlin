package com.labs64.netlicensing.provider

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap;

/**
 * Contains info about response together with response entity.
 *
 * @param <T>
 * type of response entity
</T> */
class RestResponse<T> {

    var statusCode: Int = 0

    var headers: MultiValueMap<String, Any>? = null
        set(headers) {

            field = LinkedMultiValueMap<String, Any>()
            if (headers != null) {
                this.headers!!.putAll(headers)//TODO(AY): could be problem with types
            }
        }

    var entity: T? = null

}
