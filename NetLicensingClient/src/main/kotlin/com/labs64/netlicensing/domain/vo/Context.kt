package com.labs64.netlicensing.domain.vo

import com.labs64.netlicensing.domain.Constants

class Context : GenericContext<String>(String::class.java) {
    fun setBaseUrl(baseUrl: String): Context {
        return this.setValue(Constants.BASE_URL, baseUrl) as Context
    }

    fun getBaseUrl(): String? {
        return getValue(Constants.BASE_URL)
    }

    fun setUsername(username: String): Context {
        return this.setValue(Constants.USERNAME, username) as Context
    }

    fun getUsername(): String? {
        return getValue(Constants.USERNAME)
    }

    fun setPassword(password: String): Context {
        return this.setValue(Constants.PASSWORD, password) as Context
    }

    fun getPassword(): String? {
        return getValue(Constants.PASSWORD)
    }

    fun setApiKey(apiKey: String): Context {
        return this.setValue(Constants.Token.API_KEY, apiKey) as Context
    }

    fun getApiKey(): String? {
        return getValue(Constants.Token.API_KEY)
    }

    fun setSecurityMode(securityMode: SecurityMode): Context {
        return this.setValue(Constants.SECURITY_MODE, securityMode.toString()) as Context
    }

    fun getSecurityMode(): SecurityMode? {
        val securityMode = getValue(Constants.SECURITY_MODE)
        return if (securityMode != null) SecurityMode.valueOf(securityMode) else null
    }

    fun setVendorNumber(vendorNumber: String): Context {
        return this.setValue(Constants.Vendor.VENDOR_NUMBER, vendorNumber) as Context
    }

    fun getVendorNumber(): String? {
        return getValue(Constants.Vendor.VENDOR_NUMBER)
    }
}
