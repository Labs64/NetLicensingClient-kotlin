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

    /**
     * Creates new product with given properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param product
     * non-null properties will be taken for the new object, null properties will either stay null, or will
     * be set to a default value, depending on property.
     * @return the newly created product object
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun create(context: Context, product: Product): Product? {
        CheckUtils.paramNotNull(product, "product")

        return NetLicensingService.getInstance()
            ?.post(context, Constants.Product.ENDPOINT_PATH, product.asRequestForm(), Product::class.java)
    }

    /**
     * Gets product by its number.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * the product number
     * @return the product
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
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

    /**
     * Returns products of a vendor.
     *
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param filter
     * reserved for the future use, must be omitted / set to NULL
     * @return collection of product entities or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<Product>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.Product.ENDPOINT_PATH, params, Product::class.java)
    }

    /**
     * Updates product properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * product number
     * @param product
     * non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated product.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun update(context: Context, number: String, product: Product?): Product? {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotNull(product, "product")

        return NetLicensingService.getInstance()?.post(
            context, Constants.Product.ENDPOINT_PATH + "/" + number, product!!.asRequestForm(),
            Product::class.java
        )
    }

    /**
     * Deletes product.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * product number
     * @param forceCascade
     * if true, any entities that depend on the one being deleted will be deleted too
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun delete(context: Context, number: String, forceCascade: Boolean) {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        params[Constants.CASCADE] = forceCascade
        NetLicensingService.getInstance()!!.delete(context, Constants.Product.ENDPOINT_PATH + "/" + number, params)
    }
}