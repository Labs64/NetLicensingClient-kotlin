package com.labs64.netlicensing.domain.vo

import com.labs64.netlicensing.domain.Constants
import java.util.concurrent.ConcurrentHashMap

class ValidationParameters {

    var productNumber: String? = null

    var licenseeProperties: MutableMap<String, String>? = null
        get() {
            if (field == null) {
                this.licenseeProperties = ConcurrentHashMap()
            }
            return field
        }

    var licenseeName: String?
        get() {
            return licenseeProperties?.get(Constants.Licensee.PROP_LICENSEE_NAME)
        }
        set(value) {
            if (value != null) {
                licenseeProperties?.put(Constants.Licensee.PROP_LICENSEE_NAME, value)
            } else {
                licenseeProperties?.remove(Constants.Licensee.PROP_LICENSEE_NAME)
            }
        }

    var licenseeSecret: String?
        get() {
            return licenseeProperties?.get(Constants.License.PROP_LICENSEE_SECRET)
        }
        set(value) {
            if (value != null) {
                licenseeProperties?.put(Constants.License.PROP_LICENSEE_SECRET, value)
            } else {
                licenseeProperties?.remove(Constants.License.PROP_LICENSEE_SECRET)
            }
        }

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
