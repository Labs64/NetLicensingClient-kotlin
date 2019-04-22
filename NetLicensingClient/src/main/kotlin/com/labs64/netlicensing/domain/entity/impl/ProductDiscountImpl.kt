package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.entity.Product
import com.labs64.netlicensing.domain.entity.ProductDiscount
import com.labs64.netlicensing.domain.vo.Currency
import com.labs64.netlicensing.domain.vo.Money
import java.io.Serializable
import java.math.BigDecimal
import javax.xml.bind.DatatypeConverter

class ProductDiscountImpl : ProductDiscount, Serializable {

    override var product: Product? = null

    override var totalPrice: BigDecimal? = null

    override var currency: String? = null

    override var amountFix: BigDecimal? = null
        set(amountFix) {
            amountFix?.let {
                field = amountFix
                amountPercent = null
            } ?: run {
                field = amountFix
            }
        }

    override var amountPercent: BigDecimal? = null
        set(amountPercent) {
            amountPercent?.let {
                field = amountPercent
                amountFix = null
            } ?: run {
                field = amountPercent
            }
        }

    var stringAmount: String
        get() {
            if (amountFix != null) {
                return amountFix.toString()
            }
            return if (amountPercent != null) {
                amountPercent.toString() + "%"
            } else "Error"
        }
        set(amount) = if (amount.endsWith("%")) {
            try {
                amountPercent = DatatypeConverter.parseDecimal(amount.substring(0, amount.length - 1))
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException(
                    "Format for discount amount in percent is not correct, expected numeric format"
                )
            }
        } else {
            val amountFix = Money.convertPrice(amount, Currency.EUR.name)
            this.amountFix = amountFix.amount!!
        }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(totalPrice)
        builder.append(";")
        builder.append(currency)
        builder.append(";")
        builder.append(stringAmount)
        return builder.toString()
    }

    override fun compareTo(productDiscount: ProductDiscount): Int {
        return productDiscount.totalPrice!!.compareTo(totalPrice!!) // reverse order!
    }
}