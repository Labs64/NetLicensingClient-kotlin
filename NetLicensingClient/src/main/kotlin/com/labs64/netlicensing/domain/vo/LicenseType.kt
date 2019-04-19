package com.labs64.netlicensing.domain.vo

enum class LicenseType() {

    FEATURE, TIMEVOLUME, FLOATING, QUANTITY;

    companion object {

        fun parseValue(value: String): LicenseType {
            for (licenseType in LicenseType.values()) {
                if (licenseType.name.equals(value, ignoreCase = true)) {
                    return licenseType
                }
            }
            throw IllegalArgumentException(value)
        }

        fun parseValueSafe(value: String): LicenseType? {
            try {
                return parseValue(value)
            } catch (e: IllegalArgumentException) {
                return null
            }
        }
    }
}