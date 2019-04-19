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

    /**
     * Gets payment method by its number.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * the payment method number
     * @return the payment method
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): PaymentMethod? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.PaymentMethod.ENDPOINT_PATH + "/" + number, params, PaymentMethod::class.java)
    }

    /**
     * Returns payment methods of a vendor.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param filter
     * reserved for the future use, must be omitted / set to NULL
     * @return collection of payment method entities or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<PaymentMethod>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.PaymentMethod.ENDPOINT_PATH, params, PaymentMethod::class.java)
    }

    /**
     * Updates payment method properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * payment method number
     * @param paymentMethod
     * non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated PaymentMethod.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
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