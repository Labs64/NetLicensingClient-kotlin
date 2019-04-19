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

    /**
     * Creates new licensee object with given properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param productNumber
     * parent product to which the new licensee is to be added
     * @param licensee
     * non-null properties will be taken for the new object, null properties will either stay null, or will
     * be set to a default value, depending on property.
     * @return the newly created licensee object
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun create(context: Context, productNumber: String, licensee: Licensee): Licensee? {
        CheckUtils.paramNotNull(licensee, "licensee")

        val form = licensee.asRequestForm()
        if (StringUtils.isNotBlank(productNumber)) {
            form.param(Constants.Product.PRODUCT_NUMBER, productNumber)
        }
        return NetLicensingService.getInstance()?.post(context, Constants.Licensee.ENDPOINT_PATH, form, Licensee::class.java)
    }

    /**
     * Gets licensee by its number.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * the licensee number
     * @return the licensee
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): Licensee? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.Licensee.ENDPOINT_PATH + "/" + number, params, Licensee::class.java)
    }

    /**
     * Returns all licensees of a vendor.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param filter
     * reserved for the future use, must be omitted / set to NULL
     * @return list of licensees (of all products) or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<Licensee>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()?.list(context, Constants.Licensee.ENDPOINT_PATH, params, Licensee::class.java)
    }

    /**
     * Updates licensee properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * licensee number
     * @param licensee
     * non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated licensee.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun update(context: Context, number: String, licensee: Licensee): Licensee? {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotNull(licensee, "licensee")

        return NetLicensingService.getInstance()?.post(
            context, Constants.Licensee.ENDPOINT_PATH + "/" + number, licensee.asRequestForm(),
            Licensee::class.java
        )
    }

    /**
     * Deletes licensee.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * licensee number
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
        NetLicensingService.getInstance()?.delete(context, Constants.Licensee.ENDPOINT_PATH + "/" + number, params)
    }

    /**
     * Validates active licenses of the licensee.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * licensee number
     * @param validationParameters
     * optional validation parameters. See ValidationParameters and licensing model documentation for
     * details.
     * @param meta
     * optional parameter, receiving messages returned within response <infos> section.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
    </infos> */
    @Throws(NetLicensingException::class)
    fun validate(
        context: Context,
        number: String,
        validationParameters: ValidationParameters?,
        vararg meta: MetaInfo
    ): ValidationResult? {
        CheckUtils.paramNotEmpty(number, "number")

        val form = Form()
        if (validationParameters != null) {
            if (StringUtils.isNotBlank(validationParameters.productNumber)) {
                form.param(Constants.Product.PRODUCT_NUMBER, validationParameters.productNumber)
            }
            if (StringUtils.isNotBlank(validationParameters.licenseeName)) {
                form.param(Constants.Licensee.PROP_LICENSEE_NAME, validationParameters.licenseeName)
            }
            if (StringUtils.isNotBlank(validationParameters.licenseeSecret)) {
                form.param(Constants.Licensee.PROP_LICENSEE_SECRET, validationParameters.licenseeSecret)
            }
            var pmIndex = 0
            validationParameters.parameters?.forEach { productModuleValidationParams ->
                form.param(
                    Constants.ProductModule.PRODUCT_MODULE_NUMBER + Integer.toString(pmIndex),
                    productModuleValidationParams.key
                )
                for (param in productModuleValidationParams.value.entries) {
                    form.param(param.key + Integer.toString(pmIndex), param.value)
                }
                ++pmIndex
            }
        }
        return NetLicensingService.getInstance()?.post(
            context,
            Constants.Licensee.ENDPOINT_PATH + "/" + number + "/" + Constants.Licensee.ENDPOINT_PATH_VALIDATE, form,
            ValidationResult::class.java, *meta
        )
    }

    /**
     * Transfer licenses between licensees.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * the number of the licensee receiving licenses
     * @param sourceLicenseeNumber
     * the number of the licensee delivering licenses
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
     */
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