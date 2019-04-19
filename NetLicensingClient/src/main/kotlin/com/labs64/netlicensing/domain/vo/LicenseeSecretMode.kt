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

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Enum#toString()
     */
    override fun toString(): String {
        return name
    }
}