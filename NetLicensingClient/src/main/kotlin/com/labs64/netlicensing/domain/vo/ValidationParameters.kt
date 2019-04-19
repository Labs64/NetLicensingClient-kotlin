package com.labs64.netlicensing.domain.vo

import java.util.concurrent.ConcurrentHashMap

class ValidationParameters {

    /**
     * Sets the target product
     *
     * @param productNumber
     * optional productNumber, must be provided in case licensee auto-create is enabled
     */
    var productNumber: String? = null
    /**
     * Sets the name for the new licensee
     *
     * @param licenseeName
     * optional human-readable licensee name in case licensee will be auto-created. This parameter must not
     * be the name, but can be used to store any other useful string information with new licensees, up to
     * 1000 characters.
     */
    var licenseeName: String? = null
    /**
     * Sets the licensee secret
     *
     * @param licenseeSecret
     * licensee secret stored on the client side. Refer to Licensee Secret documentation for details.
     */
    var licenseeSecret: String? = null

    var parameters: MutableMap<String, MutableMap<String, String>>? = null
        get() {
            if (field == null) {
                this.parameters = ConcurrentHashMap()
            }
            return field
        }

    fun getProductModuleValidationParameters(productModuleNumber: String): MutableMap<String, String>? {
        if (!parameters?.containsKey(productModuleNumber)!!) {
            parameters?.set(productModuleNumber, ConcurrentHashMap())
        }
        return parameters?.get(productModuleNumber)
    }

    fun setProductModuleValidationParameters(
        productModuleNumber: String,
        productModuleParameters: MutableMap<String, String>
    ) {
        parameters?.set(productModuleNumber, productModuleParameters)
    }

    fun put(productModuleNumber: String, key: String, value: String) {
        getProductModuleValidationParameters(productModuleNumber)?.set(key, value)
    }
}
