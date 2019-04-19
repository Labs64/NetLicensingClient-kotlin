package com.labs64.netlicensing.domain.entity

import com.labs64.netlicensing.domain.vo.Currency
import com.labs64.netlicensing.domain.vo.TransactionSource
import com.labs64.netlicensing.domain.vo.TransactionStatus
import java.math.BigDecimal
import java.util.Date

interface Transaction : BaseEntity {

    var status: TransactionStatus?

    var source: TransactionSource?

    var grandTotal: BigDecimal?

    var discount: BigDecimal?

    var currency: Currency?

    var dateCreated: Date?

    var dateClosed: Date?

    var licenseTransactionJoins: List<LicenseTransactionJoin>?
}
