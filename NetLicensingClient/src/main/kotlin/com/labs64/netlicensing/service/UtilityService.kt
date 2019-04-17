package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Country
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.exception.NetLicensingException
import org.apache.commons.lang3.StringUtils
import java.util.HashMap

object UtilityService {

    /**
     * Returns all countries.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param filter
     * reserved for the future use, must be omitted / set to NULL
     * @return collection of available countries or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun listCountries(context: Context, filter: String?): Page<Country>? {

        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()?.list(
            context,
            Constants.Utility.ENDPOINT_PATH + "/" + Constants.Country.ENDPOINT_PATH, params,
            Country::class.java
        )
    }
}