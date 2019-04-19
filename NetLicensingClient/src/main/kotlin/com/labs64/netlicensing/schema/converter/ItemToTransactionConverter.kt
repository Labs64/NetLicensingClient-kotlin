package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.LicenseTransactionJoin
import com.labs64.netlicensing.domain.entity.Transaction
import com.labs64.netlicensing.domain.entity.impl.LicenseImpl
import com.labs64.netlicensing.domain.entity.impl.LicenseTransactionJoinImpl
import com.labs64.netlicensing.domain.entity.impl.TransactionImpl
import com.labs64.netlicensing.domain.vo.Currency
import com.labs64.netlicensing.domain.vo.TransactionSource
import com.labs64.netlicensing.domain.vo.TransactionStatus
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item
import com.labs64.netlicensing.util.DateUtils
import java.util.ArrayList
import javax.xml.bind.DatatypeConverter

/**
 * Convert [Item] entity into [Transaction] object.
 */
class ItemToTransactionConverter : ItemToEntityBaseConverter<Transaction>() {

    @Throws(ConversionException::class)
    override fun convert(source: Item): Transaction {
        val target = super.convert(source)

        target.status = TransactionStatus.valueOf(
            SchemaFunction.propertyByName(
                source.property,
                Constants.Transaction.STATUS
            )
                .value
        )
        target.source = TransactionSource.valueOf(
            SchemaFunction.propertyByName(
                source.property,
                Constants.Transaction.SOURCE
            )
                .value
        )
        if (SchemaFunction.propertyByName(source.property, Constants.Transaction.GRAND_TOTAL).value != null) {
            target.grandTotal = DatatypeConverter.parseDecimal(
                SchemaFunction.propertyByName(
                    source.property,
                    Constants.Transaction.GRAND_TOTAL
                ).value
            )
        }
        if (SchemaFunction.propertyByName(source.property, Constants.DISCOUNT).value != null) {
            target.discount = DatatypeConverter.parseDecimal(
                SchemaFunction.propertyByName(
                    source.property,
                    Constants.DISCOUNT
                ).value
            )
        }
        if (SchemaFunction.propertyByName(source.property, Constants.CURRENCY).value != null) {
            target.currency = Currency.valueOf(
                SchemaFunction.propertyByName(
                    source.property,
                    Constants.CURRENCY
                ).value
            )
        }
        if (SchemaFunction.propertyByName(source.property, Constants.Transaction.DATE_CREATED).value != null) {
            target.dateCreated = DateUtils.parseDate(
                SchemaFunction.propertyByName(
                    source.property, Constants.Transaction.DATE_CREATED
                ).value
            ).time
        }
        if (SchemaFunction.propertyByName(source.property, Constants.Transaction.DATE_CLOSED).value != null) {
            target.dateClosed = DateUtils.parseDate(
                SchemaFunction.propertyByName(
                    source.property, Constants.Transaction.DATE_CLOSED
                ).value
            ).time
        }

        // Custom properties
        for (property in source.property) {
            if (!TransactionImpl.reservedProps.contains(property.name)) {
                target.properties!![property.name] = property.value
            }
        }

        val licenseTransactionJoins = ArrayList<LicenseTransactionJoin>()
        for (list in source.list) {
            if (Constants.Transaction.LICENSE_TRANSACTION_JOIN == list.name) {
                val licenseTransactionJoin = LicenseTransactionJoinImpl()
                val transaction = TransactionImpl()
                transaction.number = SchemaFunction.propertyByName(list.property, Constants.Transaction.TRANSACTION_NUMBER).value
                licenseTransactionJoin.transaction = transaction

                val license = LicenseImpl()
                license.number = SchemaFunction.propertyByName(list.property, Constants.License.LICENSE_NUMBER).value
                licenseTransactionJoin.license = license
                licenseTransactionJoins.add(licenseTransactionJoin)
            }
        }
        target.licenseTransactionJoins = licenseTransactionJoins

        return target
    }

    public override fun newTarget(): Transaction {
        return TransactionImpl()
    }
}