package com.labs64.netlicensing.domain.vo

import org.apache.commons.lang3.StringUtils

enum class Currency() {
    NONE, EUR;

    companion object {

        fun parseValue(value: String?): Currency {
            for (currency in Currency.values()) {
                if (currency.name.equals(value!!, ignoreCase = true)) {
                    return currency
                }
            }
            if (value != null && StringUtils.isBlank(value)) {
                return NONE
            }
            throw IllegalArgumentException(value)
        }

        fun parseValueSafe(value: String): Currency? {
            try {
                return parseValue(value)
            } catch (e: IllegalArgumentException) {
                return null
            }
        }
    }
}