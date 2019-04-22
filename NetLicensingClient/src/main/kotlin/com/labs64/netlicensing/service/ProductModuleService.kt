package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.ProductModule
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.util.CheckUtils
import org.apache.commons.lang3.StringUtils
import java.util.HashMap

object ProductModuleService {

    @Throws(NetLicensingException::class)
    fun create(
        context: Context,
        productNumber: String,
        productModule: ProductModule
    ): ProductModule? {
        CheckUtils.paramNotNull(productModule, "productNumber")

        val form = productModule.asRequestForm()
        if (StringUtils.isNotBlank(productNumber)) {
            form.param(Constants.Product.PRODUCT_NUMBER, productNumber)
        }
        return NetLicensingService.getInstance()
            ?.post(context, Constants.ProductModule.ENDPOINT_PATH, form, ProductModule::class.java)
    }

    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): ProductModule? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.ProductModule.ENDPOINT_PATH + "/" + number, params, ProductModule::class.java)
    }

    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<ProductModule>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.ProductModule.ENDPOINT_PATH, params, ProductModule::class.java)
    }

    @Throws(NetLicensingException::class)
    fun update(
        context: Context,
        number: String,
        productModule: ProductModule
    ): ProductModule? {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotNull(productModule, "productModule")

        return NetLicensingService.getInstance()?.post(
            context, Constants.ProductModule.ENDPOINT_PATH + "/" + number,
            productModule.asRequestForm(), ProductModule::class.java
        )
    }

    @Throws(NetLicensingException::class)
    fun delete(context: Context, number: String, forceCascade: Boolean) {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        params[Constants.CASCADE] = forceCascade
        NetLicensingService.getInstance()?.delete(context, Constants.ProductModule.ENDPOINT_PATH + "/" + number, params)
    }
}