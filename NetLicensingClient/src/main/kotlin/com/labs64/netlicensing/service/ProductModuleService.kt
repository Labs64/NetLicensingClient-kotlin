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

    /**
     * Creates new product module object with given properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param productNumber
     * parent product to which the new product module is to be added
     * @param productModule
     * non-null properties will be taken for the new object, null properties will either stay null, or will
     * be set to a default value, depending on property.
     * @return the newly created product module object
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
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

    /**
     * Gets product module by its number.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * the product module number
     * @return the product module
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): ProductModule? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.ProductModule.ENDPOINT_PATH + "/" + number, params, ProductModule::class.java)
    }

    /**
     * Returns all product modules of a vendor.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param filter
     * reserved for the future use, must be omitted / set to NULL
     * @return list of product modules (of all products) or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<ProductModule>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.ProductModule.ENDPOINT_PATH, params, ProductModule::class.java)
    }

    /**
     * Updates product module properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * product module number
     * @param productModule
     * non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated product module.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
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

    /**
     * Deletes product module.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * product module number
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
        NetLicensingService.getInstance()?.delete(context, Constants.ProductModule.ENDPOINT_PATH + "/" + number, params)
    }
}