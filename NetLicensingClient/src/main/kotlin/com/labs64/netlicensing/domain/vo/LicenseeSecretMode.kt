package com.labs64.netlicensing.domain.vo

enum class LicenseeSecretMode {

    DISABLED, PREDEFINED, CLIENT;

    fun parseString(value: String): LicenseeSecretMode {
        for (licenseeSecretMode in LicenseeSecretMode.values()) {
            if (licenseeSecretMode.name.equals(value, ignoreCase = true)) {
                return licenseeSecretMode
            }
        }
        return LicenseeSecretMode.DISABLED
    }

    override fun toString(): String {
        return name
    }
}