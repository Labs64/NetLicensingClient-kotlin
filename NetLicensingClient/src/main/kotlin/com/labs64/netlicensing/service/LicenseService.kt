package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.License
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.util.CheckUtils
import org.apache.commons.lang3.StringUtils
import java.util.HashMap

object LicenseService {

    /**
     * Creates new license object with given properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param licenseeNumber
     * parent licensee to which the new license is to be added
     * @param licenseTemplateNumber
     * license template that the license is created from
     * @param transactionNumber
     * For privileged logins specifies transaction for the license creation. For regular logins new
     * transaction always created implicitly, and the operation will be in a separate transaction.
     * Transaction is generated with the provided transactionNumber, or, if transactionNumber is null, with
     * auto-generated number.
     * @param license
     * non-null properties will be taken for the new object, null properties will either stay null, or will
     * be set to a default value, depending on property.
     * @return the newly created license object
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun create(
        context: Context,
        licenseeNumber: String,
        licenseTemplateNumber: String,
        transactionNumber: String?,
        license: License
    ): License? {

        CheckUtils.paramNotNull(license, "license")

        val form = license.asRequestForm()
        if (StringUtils.isNotBlank(licenseeNumber)) {
            form.param(Constants.Licensee.LICENSEE_NUMBER, licenseeNumber)
        }
        if (StringUtils.isNotBlank(licenseTemplateNumber)) {
            form.param(Constants.LicenseTemplate.LICENSE_TEMPLATE_NUMBER, licenseTemplateNumber)
        }
        if (StringUtils.isNotBlank(transactionNumber)) {
            form.param(Constants.Transaction.TRANSACTION_NUMBER, transactionNumber)
        }
        return NetLicensingService.getInstance()
            ?.post(context, Constants.License.ENDPOINT_PATH, form, License::class.java)
    }

    /**
     * Gets license by its number.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * the license number
     * @return the license
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): License? {
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.License.ENDPOINT_PATH + "/" + number, params, License::class.java)
    }

    /**
     * Returns licenses of a vendor.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param filter
     * reserved for the future use, must be omitted / set to NULL
     * @return list of licenses (of all products) or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<License>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()
            ?.list(context, Constants.License.ENDPOINT_PATH, params, License::class.java)
    }

    /**
     * Updates license properties.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * license number
     * @param transactionNumber
     * transaction for the license update. Created implicitly if transactionNumber is null. In this case the
     * operation will be in a separate transaction.
     * @param license
     * non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated license.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These exceptions will be transformed to the
     * corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun update(
        context: Context,
        number: String,
        transactionNumber: String?,
        license: License
    ): License? {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotNull(license, "license")

        val form = license.asRequestForm()
        if (StringUtils.isNotBlank(transactionNumber)) {
            form.param(Constants.Transaction.TRANSACTION_NUMBER, transactionNumber)
        }
        return NetLicensingService.getInstance()
            ?.post(context, Constants.License.ENDPOINT_PATH + "/" + number, form, License::class.java)
    }

    /**
     * Deletes license.
     *
     *
     * When any license is deleted, corresponding transaction is created automatically.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * license number
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
        NetLicensingService.getInstance()?.delete(context, Constants.License.ENDPOINT_PATH + "/" + number, params)
    }
}