package com.labs64.netlicensing.exception

/**
 * The Class RestException can be used in cases where no checked exception can be thrown (e.g. 3pp interfaces
 * implementation).
 */
class RestException(message: String) : NetLicensingException(message)