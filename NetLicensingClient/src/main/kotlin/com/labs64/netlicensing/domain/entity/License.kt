package com.labs64.netlicensing.domain.entity

import com.labs64.netlicensing.domain.vo.Currency
import java.math.BigDecimal

interface License : BaseEntity {

    var name: String?

    var price: BigDecimal?

    var currency: Currency?

    var hidden: Boolean?

    var licensee: Licensee?

    var licenseTemplate: LicenseTemplate?

    var licenseTransactionJoins: List<LicenseTransactionJoin>?
}
