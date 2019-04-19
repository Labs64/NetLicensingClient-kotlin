package com.labs64.netlicensing.domain.vo

import com.labs64.netlicensing.domain.Constants
import org.apache.commons.lang3.StringUtils
import java.math.BigDecimal
import javax.xml.bind.DatatypeConverter

object Money {

    var amount: BigDecimal? = null

    var currencyCode: String? = null

    fun convertPrice(rawPrice: String, rawCurrency: String): Money {
        val target = this
        if (StringUtils.isNotBlank(rawPrice)) {
            try {
                target.amount = DatatypeConverter.parseDecimal(rawPrice)
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException(
                    "'" + Constants.PRICE + "' format is not correct, expected '0.00' format"
                )
            }

            if (StringUtils.isNotBlank(rawCurrency)) {
                if (Currency.parseValueSafe(rawCurrency) == null) {
                    throw IllegalArgumentException("Unsupported currency!")
                }
                target.currencyCode = rawCurrency
            } else {
                throw IllegalArgumentException(
                    "'" + Constants.PRICE + "' field must be accompanied with the '" + Constants.CURRENCY + "' field"
                )
            }
        } else { // 'price' is not provided
            if (StringUtils.isNotBlank(rawCurrency)) {
                throw IllegalArgumentException(
                    "'" + Constants.CURRENCY + "' field can not be used without the '" + Constants.PRICE + "' field"
                )
            }
        }
        return target
    }
}