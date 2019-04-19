package com.labs64.netlicensing.util

import com.labs64.netlicensing.exception.MalformedArgumentsException

object CheckUtils {

    @Throws(MalformedArgumentsException::class)
    private fun notNull(any: Any?, msg: String) {
        if (any == null) {
            throw MalformedArgumentsException(msg)
        }
    }

    @Throws(MalformedArgumentsException::class)
    private fun notEmpty(string: String?, msg: String) {
        if (string == null || string.length == 0) {
            throw MalformedArgumentsException(msg)
        }
    }

    @Throws(MalformedArgumentsException::class)
    fun paramNotNull(parameter: Any?, parameterName: String) {
        notNull(parameter, String.format("Parameter '%s' cannot be null", parameterName))
    }

    @Throws(MalformedArgumentsException::class)
    fun paramNotEmpty(parameter: String, parameterName: String) {
        notEmpty(parameter, String.format("Parameter '%s' cannot be null or empty string", parameterName))
    }
}