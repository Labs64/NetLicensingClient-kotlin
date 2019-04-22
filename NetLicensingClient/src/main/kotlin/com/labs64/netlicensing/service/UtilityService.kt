package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Country
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.LicenseTypeProperties
import com.labs64.netlicensing.domain.vo.LicensingModelProperties
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.domain.vo.PageImpl
import com.labs64.netlicensing.exception.NetLicensingException
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.Transformer
import org.apache.commons.lang3.StringUtils

object UtilityService {

    @Suppress("UNCHECKED_CAST")
    @Throws(NetLicensingException::class)
    fun listLicenseTypes(context: Context): Page<String> {
        val params = HashMap<String, Any?>()

        val licenseTypes = NetLicensingService.getInstance()?.list(
            context,
            Constants.Utility.ENDPOINT_PATH + "/licenseTypes", params, LicenseTypeProperties::class.java
        )
        return PageImpl(
            CollectionUtils.collect(
                licenseTypes?.content,
                object : Transformer<LicenseTypeProperties, String> {

                    override fun transform(licenseType: LicenseTypeProperties): String {
                        return licenseType.name
                    }
                }) as List<String>,
            licenseTypes?.pageNumber,
            licenseTypes?.itemsNumber,
            licenseTypes?.totalPages,
            licenseTypes?.totalItems,
            licenseTypes?.hasNext()
        )
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(NetLicensingException::class)
    fun listLicensingModels(context: Context): Page<String> {
        val params = HashMap<String, Any?>()
        val licensingModels = NetLicensingService.getInstance()?.list(
            context,
            Constants.Utility.ENDPOINT_PATH + "/licensingModels", params, LicensingModelProperties::class.java
        )
        return PageImpl(
            CollectionUtils.collect(
                licensingModels?.content,
                object : Transformer<LicensingModelProperties, String> {

                    override fun transform(licenseType: LicensingModelProperties): String {
                        return licenseType.name
                    }
                }) as List<String>,
            licensingModels?.pageNumber,
            licensingModels?.itemsNumber,
            licensingModels?.totalPages,
            licensingModels?.totalItems,
            licensingModels?.hasNext()
        )
    }

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