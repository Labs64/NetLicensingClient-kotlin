package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.EntityFactory
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.MetaInfo
import com.labs64.netlicensing.domain.vo.ValidationParameters
import com.labs64.netlicensing.domain.vo.ValidationResult
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.schema.context.Netlicensing
import com.labs64.netlicensing.util.CheckUtils
import com.labs64.netlicensing.util.SignatureUtils
import org.apache.commons.lang3.StringUtils
import javax.ws.rs.core.Form

object ValidationService {
    /**
     * Validates active licenses of the licensee.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            licensee number
     * @param validationParameters
     *            optional validation parameters. See ValidationParameters and licensing model documentation for
     *            details.
     * @param meta
     *            optional parameter, receiving messages returned within response <infos> section.
     * @return result of the validation
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    @Throws(NetLicensingException::class)
    fun validate(context: Context, number: String, validationParameters: ValidationParameters?, vararg meta: MetaInfo): ValidationResult? {
        val validationFile = retrieveValidationFile(context, number, validationParameters)

        if (validationFile === null) {
            return null
        }

        return convertValidationResult(validationFile, *meta)
    }

    /**
     * Retrieves validation file for the given licensee from the server as {@link Netlicensing} object. The file can be
     * stored locally for subsequent validation by {@link #validateOffline} method, that doesn't require connection to
     * the server.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            licensee number
     * @param validationParameters
     *            optional validation parameters. See ValidationParameters and licensing model documentation for
     *            details.
     * @return validation file, possibly signed, for subsequent use in {@link #validateOffline}
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    @Throws(NetLicensingException::class)
    fun retrieveValidationFile(context: Context, number: String, validationParameters: ValidationParameters?): Netlicensing? {
        CheckUtils.paramNotEmpty(number, "number")

        val form = convertValidationParameters(validationParameters)
        val urlTemplate = Constants.Licensee.ENDPOINT_PATH + "/" + number + "/" + Constants.Licensee.ENDPOINT_PATH_VALIDATE

        return NetLicensingService.getInstance()?.request(context, "POST", urlTemplate, form, null)
    }

    /**
     * Perform validation without connecting to the server (offline) using validation file previously retrieved by
     * {@link #retrieveValidationFile}.
     *
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param validationFile
     *            validation file returned by {@link #retrieveValidationFile} call
     * @return result of the validation
     * @throws NetLicensingException
     *             in case of a service error. Check subclass and message for details.
     */
    fun validateOffline(context: Context, validationFile: Netlicensing, vararg meta: MetaInfo): ValidationResult? {
        SignatureUtils.check(context, validationFile)
        return convertValidationResult(validationFile, *meta)
    }

    private fun convertValidationParameters(validationParameters: ValidationParameters?): Form {
        val form = Form()

        if (validationParameters !== null) {
            if (StringUtils.isNotBlank(validationParameters.productNumber)) {
                form.param(Constants.Product.PRODUCT_NUMBER, validationParameters.productNumber)
            }

            validationParameters.licenseeProperties?.forEach { (k, v) ->
                if (StringUtils.isNotBlank(k)) {
                    form.param(k, v)
                }
            }

            if (validationParameters.dryRun) {
                form.param(Constants.Validation.DRY_RUN, true.toString())
            }

            var pmIndex = 0
            for ((key, value) in validationParameters.parameters!!) {
                form.param(Constants.ProductModule.PRODUCT_MODULE_NUMBER + pmIndex.toString(), key)
                for ((key1, value1) in value) {
                    form.param(key1 + pmIndex.toString(), value1)
                }
                ++pmIndex
            }
        }
        return form
    }

    @Throws(NetLicensingException::class)
    private fun convertValidationResult(validationFile: Netlicensing, vararg meta: MetaInfo): ValidationResult? {
        if (meta.isNotEmpty() && validationFile.id != null) {
            meta[0].setValue(Constants.PROP_ID, validationFile.id)
        }

        return (EntityFactory()).create(validationFile, ValidationResult::class.java)
    }
}