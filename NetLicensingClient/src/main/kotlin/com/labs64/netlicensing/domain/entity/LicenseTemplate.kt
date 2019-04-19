package com.labs64.netlicensing.domain.entity

import com.labs64.netlicensing.domain.vo.Currency
import com.labs64.netlicensing.domain.vo.LicenseType
import java.math.BigDecimal

interface LicenseTemplate : BaseEntity {

    var name: String?

    var licenseType: LicenseType?

    var price: BigDecimal?

    var currency: Currency?

    var automatic: Boolean?

    var hidden: Boolean?

    var hideLicenses: Boolean?

    var productModule: ProductModule?

    val licenses: MutableCollection<License>?
}