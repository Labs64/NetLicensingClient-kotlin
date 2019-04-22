package com.labs64.netlicensing.exception

abstract class NetLicensingException(message: String) : Exception(message) {

    val rootCause: Throwable?
        get() {
            var rootCause: Throwable? = null
            var cause: Throwable? = cause
            while (cause != null && cause !== rootCause) {
                rootCause = cause
                cause = cause.cause
            }
            return rootCause
        }

    val mostSpecificCause: Throwable
        get() {
            val rootCause = rootCause
            return rootCause ?: this
        }

    operator fun contains(exType: Class<Throwable>?): Boolean {
        if (exType == null) {
            return false
        }
        if (exType.isInstance(this)) {
            return true
        }
        var cause: Throwable? = cause
        if (cause === this) {
            return false
        }
        if (cause is NetLicensingException) {
            return cause.contains(exType)
        } else {
            while (cause != null) {
                if (exType.isInstance(cause)) {
                    return true
                }
                if (cause.cause === cause) {
                    break
                }
                cause = cause.cause
            }
            return false
        }
    }
}