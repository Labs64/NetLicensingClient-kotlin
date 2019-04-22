package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.LicenseTemplate
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.util.CheckUtils
import org.apache.commons.lang3.StringUtils
import java.util.HashMap

object LicenseTemplateService {

    @Throws(NetLicensingException::class)
    fun create(
        context: Context,
        productModuleNumber: String,
        licenseTemplate: LicenseTemplate
    ): LicenseTemplate? {
        CheckUtils.paramNotNull(licenseTemplate, "licenseTemplate")

        val form = licenseTemplate.asRequestForm()
        if (StringUtils.isNotBlank(productModuleNumber)) {
            form.param(Constants.ProductModule.PRODUCT_MODULE_NUMBER, productModuleNumber)
        }
        return NetLicensingService.getInstance()
            ?.post(context, Constants.LicenseTemplate.ENDPOINT_PATH, form, LicenseTemplate::class.java)
    }

    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): LicenseTemplate? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.LicenseTemplate.ENDPOINT_PATH + "/" + number, params, LicenseTemplate::class.java)
    }

    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<LicenseTemplate>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.LicenseTemplate.ENDPOINT_PATH, params, LicenseTemplate::class.java)
    }

    @Throws(NetLicensingException::class)
    fun update(
        context: Context,
        number: String,
        licenseTemplate: LicenseTemplate
    ): LicenseTemplate? {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotNull(licenseTemplate, "licenseTemplate")

        return NetLicensingService.getInstance()?.post(
            context, Constants.LicenseTemplate.ENDPOINT_PATH + "/" + number,
            licenseTemplate.asRequestForm(), LicenseTemplate::class.java
        )
    }

    @Throws(NetLicensingException::class)
    fun delete(context: Context, number: String, forceCascade: Boolean) {
        CheckUtils.paramNotEmpty(number, "number")

        val params = HashMap<String, Any?>()
        params[Constants.CASCADE] = forceCascade
        NetLicensingService.getInstance()
            ?.delete(context, Constants.LicenseTemplate.ENDPOINT_PATH + "/" + number, params)
    }
}