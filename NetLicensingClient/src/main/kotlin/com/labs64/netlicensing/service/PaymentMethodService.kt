package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.PaymentMethod
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.util.CheckUtils
import org.apache.commons.lang3.StringUtils
import java.util.HashMap

object PaymentMethodService {

    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): PaymentMethod? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.PaymentMethod.ENDPOINT_PATH + "/" + number, params, PaymentMethod::class.java)
    }

    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<PaymentMethod>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.PaymentMethod.ENDPOINT_PATH, params, PaymentMethod::class.java)
    }

    @Throws(NetLicensingException::class)
    fun update(context: Context, number: String, paymentMethod: PaymentMethod): PaymentMethod? {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotNull(paymentMethod, "paymentMethod")

        return NetLicensingService.getInstance()?.post(
            context, Constants.PaymentMethod.ENDPOINT_PATH + "/" + number,
            paymentMethod.asRequestForm(), PaymentMethod::class.java
        )
    }
}