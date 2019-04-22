package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Transaction
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.util.CheckUtils
import org.apache.commons.lang3.StringUtils
import java.util.HashMap

object TransactionService {

    @Throws(NetLicensingException::class)
    fun create(context: Context, transaction: Transaction): Transaction? {
        CheckUtils.paramNotNull(transaction, "transaction")

        return NetLicensingService.getInstance()?.post(
            context, Constants.Transaction.ENDPOINT_PATH,
            transaction.asRequestForm(),
            Transaction::class.java
        )
    }

    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): Transaction? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()?.get(
            context, Constants.Transaction.ENDPOINT_PATH + "/" + number, params,
            Transaction::class.java
        )
    }

    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String?): Page<Transaction>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()?.list(
            context, Constants.Transaction.ENDPOINT_PATH, params,
            Transaction::class.java
        )
    }

    @Throws(NetLicensingException::class)
    fun update(context: Context, number: String, transaction: Transaction): Transaction? {
        CheckUtils.paramNotEmpty(number, "number")
        CheckUtils.paramNotNull(transaction, "transaction")

        return NetLicensingService.getInstance()?.post(
            context, Constants.Transaction.ENDPOINT_PATH + "/" + number,
            transaction.asRequestForm(), Transaction::class.java
        )
    }
}