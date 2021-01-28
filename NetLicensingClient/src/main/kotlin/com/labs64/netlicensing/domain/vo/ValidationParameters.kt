package com.labs64.netlicensing.domain.vo

import java.util.concurrent.ConcurrentHashMap

class ValidationParameters {

    var productNumber: String? = null

    var licenseeName: String? = null

    var licenseeSecret: String? = null

    var dryRun: Boolean = false

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
