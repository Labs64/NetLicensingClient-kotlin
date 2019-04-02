package com.labs64.netlicensing.exception

/**
 * Base class for checked exceptions with a root cause.
 */
abstract class NetLicensingException(message: String) : Exception(message) {

    /**
     * Retrieve the innermost cause of this exception, if any.
     *
     * @return the innermost exception, or `null` if none
     */
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

    /**
     * Retrieve the most specific cause of this exception, that is, either the innermost cause (root cause) or this
     * exception itself.
     *
     *
     * Differs from [.getRootCause] in that it falls back to the present exception if there is no root cause.
     *
     * @return the most specific cause (never `null`)
     */
    val mostSpecificCause: Throwable
        get() {
            val rootCause = rootCause
            return rootCause ?: this
        }

    /**
     * Check whether this exception contains an exception of the given type: either it is of the given class itself or
     * it contains a cause cause of the given type.
     *
     * @param exType
     * the exception type to look for
     * @return whether there is a cause exception of the specified type
     */
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