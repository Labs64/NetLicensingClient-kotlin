package com.labs64.netlicensing.exception

/**
 * The exception class that should be used when the content of the response of NetLicensing service doesn't meet
 * expectations that we impose on it.
 */
class WrongResponseFormatException
/**
 * Construct a `ConversionException` with the specified detail message.
 *
 * @param msg
 * the detail message
 */
    (msg: String) : NetLicensingException(msg)