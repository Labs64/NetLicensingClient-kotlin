package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Licensee
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.MetaInfo
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.domain.vo.ValidationParameters
import com.labs64.netlicensing.domain.vo.ValidationResult
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.util.CheckUtils
import org.apache.commons.lang3.StringUtils
import java.util.HashMap
import javax.ws.rs.core.Form

object LicenseeService {

    @Throws(NetLicensingException::class)
    fun create(context: Context, productNumber: String, licensee: Licensee): Licensee? {
        CheckUtils.paramNotNull(licensee, "licensee")

        val form = licensee.asRequestForm()
        if (StringUtils.isNotBlank(productNumber)) {
            form.param(Constants.Product.PRODUCT_NUMBER, productNumber)
        }
        return NetLicensingService.getInstance()
            ?.post(context, Constants.Licensee.ENDPOINT_PATH, form, Licensee::class.java)
    }

    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): Licensee? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.Licensee.ENDPOINT_PATH + "/" + number, params, Licensee::class.java)
    }

    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<Licensee>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.Licensee.ENDPOINT_PATH, params, Licensee::class.java)
    }

    @Throws(NetLicensingException::class)
    fun update(context: Context, number: String, licensee: Licensee): Licensee? {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotNull(licensee, "licensee")

        return NetLicensingService.getInstance()?.post(
            context, Constants.Licensee.ENDPOINT_PATH + "/" + number, licensee.asRequestForm(),
            Licensee::class.java
        )
    }

    @Throws(NetLicensingException::class)
    fun delete(context: Context, number: String, forceCascade: Boolean) {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        params[Constants.CASCADE] = forceCascade
        NetLicensingService.getInstance()?.delete(context, Constants.Licensee.ENDPOINT_PATH + "/" + number, params)
    }

    @Throws(NetLicensingException::class)
    fun validate(
        context: Context,
        number: String,
        validationParameters: ValidationParameters?,
        vararg meta: MetaInfo
    ): ValidationResult? {
        CheckUtils.paramNotEmpty(number, "number")

        return ValidationService.validate(context, number, validationParameters, *meta)
    }

    @Throws(NetLicensingException::class)
    fun transfer(context: Context, number: String, sourceLicenseeNumber: String) {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotEmpty(sourceLicenseeNumber, Constants.Licensee.SOURCE_LICENSEE_NUMBER)

        val form = Form()
        form.param(Constants.Licensee.SOURCE_LICENSEE_NUMBER, sourceLicenseeNumber)

        NetLicensingService.getInstance()?.post(
            context,
            Constants.Licensee.ENDPOINT_PATH + "/" + number + "/" + Constants.Licensee.ENDPOINT_PATH_TRANSFER, form,
            Licensee::class.java
        )
    }
}