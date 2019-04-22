package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Product
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.util.CheckUtils
import org.apache.commons.lang3.StringUtils
import java.util.HashMap

object ProductService {

    @Throws(NetLicensingException::class)
    fun create(context: Context, product: Product): Product? {
        CheckUtils.paramNotNull(product, "product")

        return NetLicensingService.getInstance()
            ?.post(context, Constants.Product.ENDPOINT_PATH, product.asRequestForm(), Product::class.java)
    }

    @Throws(NetLicensingException::class)
    fun get(context: Context, number: String): Product {
        val params = HashMap<String, Any?>()
        CheckUtils.paramNotEmpty(number, "number")

        return NetLicensingService.getInstance()!!.get(
            context,
            Constants.Product.ENDPOINT_PATH + "/" + number,
            params,
            Product::class.java
        )
    }

    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<Product>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.Product.ENDPOINT_PATH, params, Product::class.java)
    }

    @Throws(NetLicensingException::class)
    fun update(context: Context, number: String, product: Product?): Product? {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotNull(product, "product")

        return NetLicensingService.getInstance()?.post(
            context, Constants.Product.ENDPOINT_PATH + "/" + number, product!!.asRequestForm(),
            Product::class.java
        )
    }

    @Throws(NetLicensingException::class)
    fun delete(context: Context, number: String, forceCascade: Boolean) {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        params[Constants.CASCADE] = forceCascade
        NetLicensingService.getInstance()!!.delete(context, Constants.Product.ENDPOINT_PATH + "/" + number, params)
    }
}