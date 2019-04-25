package com.labs64.netlicensing.domain.vo

enum class LicenseeSecretMode {

    PREDEFINED, CLIENT;

    fun parseString(value: String): LicenseeSecretMode {
        for (licenseeSecretMode in LicenseeSecretMode.values()) {
            if (licenseeSecretMode.name.equals(value, ignoreCase = true)) {
                return licenseeSecretMode
            }
        }
        return LicenseeSecretMode.PREDEFINED
    }

    override fun toString(): String {
        return name
    }
}