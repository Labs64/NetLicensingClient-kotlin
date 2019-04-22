package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.License
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.util.CheckUtils
import org.apache.commons.lang3.StringUtils
import java.util.HashMap

object LicenseService {

    @Throws(NetLicensingException::class)
    fun create(
        context: Context,
        licenseeNumber: String,
        licenseTemplateNumber: String,
        transactionNumber: String?,
        license: License
    ): License? {

        CheckUtils.paramNotNull(license, "license")

        val form = license.asRequestForm()
        if (StringUtils.isNotBlank(licenseeNumber)) {
            form.param(Constants.Licensee.LICENSEE_NUMBER, licenseeNumber)
        }
        if (StringUtils.isNotBlank(licenseTemplateNumber)) {
            form.param(Constants.LicenseTemplate.LICENSE_TEMPLATE_NUMBER, licenseTemplateNumber)
        }
        if (StringUtils.isNotBlank(transactionNumber)) {
            form.param(Constants.Transaction.TRANSACTION_NUMBER, transactionNumber)
        }
        return NetLicensingService.getInstance()
            ?.post(context, Constants.License.ENDPOINT_PATH, form, License::class.java)
    }

    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): License? {
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.License.ENDPOINT_PATH + "/" + number, params, License::class.java)
    }

    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<License>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.License.ENDPOINT_PATH, params, License::class.java)
    }

    @Throws(NetLicensingException::class)
    fun update(
        context: Context,
        number: String,
        transactionNumber: String?,
        license: License
    ): License? {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotNull(license, "license")

        val form = license.asRequestForm()
        if (StringUtils.isNotBlank(transactionNumber)) {
            form.param(Constants.Transaction.TRANSACTION_NUMBER, transactionNumber)
        }
        return NetLicensingService.getInstance()
            ?.post(context, Constants.License.ENDPOINT_PATH + "/" + number, form, License::class.java)
    }

    @Throws(NetLicensingException::class)
    fun delete(context: Context, number: String, forceCascade: Boolean) {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        params[Constants.CASCADE] = forceCascade
        NetLicensingService.getInstance()?.delete(context, Constants.License.ENDPOINT_PATH + "/" + number, params)
    }
}