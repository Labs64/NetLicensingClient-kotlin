package com.labs64.netlicensing.domain.vo

import org.apache.commons.lang3.StringUtils

enum class Currency(value: String) {
    NONE(""), EUR("EUR");

    companion object {
        /**
         * Parse currency value to [Currency] enum.
         *
         * @param value
         * currency value
         * @return [Currency] enum object or throws [IllegalArgumentException] if no corresponding
         * [Currency] enum object found
         */
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

        /**
         * Parse currency value to [Currency] enum, nothrow version.
         *
         * @param value
         * licenseType value as string
         * @return [Currency] enum object or `null` if argument doesn't match any of the enum values
         */
        fun parseValueSafe(value: String): Currency? {
            try {
                return parseValue(value)
            } catch (e: IllegalArgumentException) {
                return null
            }
        }
    }
}