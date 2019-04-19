package com.labs64.netlicensing.domain.vo

import com.labs64.netlicensing.domain.Constants
import java.io.Serializable
import java.util.Calendar
import java.util.HashMap
import java.util.TimeZone

class ValidationResult : Serializable {

    var ttl: Calendar? = null

    init {
        ttl = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        ttl!!.add(Calendar.MINUTE, Constants.ValidationResult.DEFAULT_TTL_MINUTES)
    }

    fun getProductModuleValidation(productModuleNumber: String): Composition? {
        return validations!![productModuleNumber]
    }

    private var validations: MutableMap<String, Composition>? = null
        get() {
            if (field == null) {
                this.validations = HashMap<String, Composition>()
            }
            return field
        }

    fun setProductModuleValidation(
        productModuleNumber: String,
        productModuleValidation: Composition
    ) {
        validations!![productModuleNumber] = productModuleValidation
    }

    fun put(productModuleNumber: String, key: String, value: String) {
        getProductModuleValidation(productModuleNumber)!!.put(key, value)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("ValidationResult [")
        var first = true
        for (validationEntry in validations!!.entries) {
            if (first) {
                first = false
            } else {
                builder.append(", ")
            }
            builder.append("ProductModule<")
            builder.append(validationEntry.key)
            builder.append(">")
            builder.append(validationEntry.value.toString())
        }
        builder.append("]")
        return builder.toString()
    }
}
