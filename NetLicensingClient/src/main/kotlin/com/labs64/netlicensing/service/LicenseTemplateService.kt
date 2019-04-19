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

    /**
     * Creates new license template object with given properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param productModuleNumber
     * parent product module to which the new license template is to be added
     * @param licenseTemplate
     * non-null properties will be taken for the new object, null properties will either stay null, or will
     * be set to a default value, depending on property.
     * @return the newly created license template object
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
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

    /**
     * Gets license template by its number.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * the license template number
     * @return the license template
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): LicenseTemplate? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.LicenseTemplate.ENDPOINT_PATH + "/" + number, params, LicenseTemplate::class.java)
    }

    /**
     * Returns all license templates of a vendor.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param filter
     * reserved for the future use, must be omitted / set to NULL
     * @return list of license templates (of all products/modules) or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<LicenseTemplate>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.LicenseTemplate.ENDPOINT_PATH, params, LicenseTemplate::class.java)
    }

    /**
     * Updates license template properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * license template number
     * @param licenseTemplate
     * non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated license template.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
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

    /**
     * Deletes license template.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * license template number
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
        NetLicensingService.getInstance()
            ?.delete(context, Constants.LicenseTemplate.ENDPOINT_PATH + "/" + number, params)
    }
}