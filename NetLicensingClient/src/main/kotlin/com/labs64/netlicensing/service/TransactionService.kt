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

    /**
     * Creates new transaction object with given properties.
     *
     *
     * This routine is for internal use by NetLicensing. Where appropriate, transactions will be created by NetLicensing
     * automatically.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param transaction
     * non-null properties will be taken for the new object, null properties will either stay null, or will
     * be set to a default value, depending on property.
     * @return the newly created transaction object
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun create(context: Context, transaction: Transaction): Transaction? {
        CheckUtils.paramNotNull(transaction, "transaction")

        return NetLicensingService.getInstance()?.post(
            context, Constants.Transaction.ENDPOINT_PATH,
            transaction.asRequestForm(),
            Transaction::class.java
        )
    }

    /**
     * Gets transaction by its number.
     *
     *
     * Use this operation for getting details about certain transaction. List of all transactions can be obtained by the
     * [.list] operation.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * the transaction number
     * @return the transaction
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): Transaction? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()?.get(
            context, Constants.Transaction.ENDPOINT_PATH + "/" + number, params,
            Transaction::class.java
        )
    }

    /**
     * Returns all transactions of a vendor.
     *
     *
     * Use this operation to get the list of all transactions.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param filter
     * reserved for the future use, must be omitted / set to NULL
     * @return list of transactions (of all products/licensees) or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
     */
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

    /**
     * Updates transaction properties.
     *
     *
     * This routine is for internal use by NetLicensing. Where appropriate, transactions will be modified by
     * NetLicensing automatically.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * transaction number
     * @param transaction
     * non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return updated transaction.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
     */
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