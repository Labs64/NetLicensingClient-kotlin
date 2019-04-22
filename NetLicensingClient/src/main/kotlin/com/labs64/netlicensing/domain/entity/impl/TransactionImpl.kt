package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.LicenseTransactionJoin
import com.labs64.netlicensing.domain.entity.Transaction
import com.labs64.netlicensing.domain.vo.Currency
import com.labs64.netlicensing.domain.vo.TransactionSource
import com.labs64.netlicensing.domain.vo.TransactionStatus
import java.math.BigDecimal
import java.util.ArrayList
import java.util.Date
import javax.ws.rs.core.MultivaluedMap

class TransactionImpl : BaseEntityImpl(), Transaction {

    override var status: TransactionStatus? = null

    override var source: TransactionSource? = null

    override var grandTotal: BigDecimal? = null

    override var discount: BigDecimal? = null

    override var currency: Currency? = null

    override var dateCreated: Date? = null
        set(dateCreated) {
            if (dateCreated == null) {
                field = Date()
            } else {
                field = Date(dateCreated.time)
            }
        }
        get() {
            if (field == null) {
                return null
            }
            return Date(field!!.time)
        }

    override var dateClosed: Date? = null
        set(dateClosed) {
            if (dateClosed == null) {
                field = Date()
            } else {
                field = Date(dateClosed.time)
            }
        }
        get() {
            if (field == null) {
                return null
            }
            return Date(field!!.time)
        }

    override var licenseTransactionJoins: List<LicenseTransactionJoin>? = null
        get() {
            if (field == null) {
                this.licenseTransactionJoins = ArrayList()
            }
            return field
        }

    val transactionProperties: Map<String, String>?
        get() = properties

    override fun asPropertiesMap(): MultivaluedMap<String, Any> {
        val map = super.asPropertiesMap()
        map.add(Constants.Transaction.STATUS, status)
        map.add(Constants.Transaction.SOURCE, source)
        map.add(Constants.Transaction.GRAND_TOTAL, grandTotal)
        map.add(Constants.DISCOUNT, discount)
        map.add(Constants.CURRENCY, currency)
        return map
    }

    companion object {
        val reservedProps: List<String>
            get() {
                val reserved = BaseEntityImpl.reservedProps
                reserved.add(Constants.Transaction.SOURCE)
                reserved.add(Constants.Transaction.STATUS)
                reserved.add(Constants.Transaction.DATE_CREATED)
                reserved.add(Constants.Transaction.DATE_CLOSED)
                reserved.add(Constants.IN_USE)
                reserved.add(Constants.Transaction.GRAND_TOTAL)
                reserved.add(Constants.DISCOUNT)
                reserved.add(Constants.CURRENCY)
                return reserved
            }
    }
}