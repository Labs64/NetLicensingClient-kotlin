package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Product
import com.labs64.netlicensing.domain.entity.ProductDiscount
import com.labs64.netlicensing.domain.entity.impl.ProductDiscountImpl
import com.labs64.netlicensing.domain.entity.impl.ProductImpl
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item
import java.util.Collections
import javax.xml.bind.DatatypeConverter

class ItemToProductConverter : ItemToEntityBaseConverter<Product>() {

    @Throws(ConversionException::class)
    override fun convert(source: Item?): Product {
        val target = super.convert(source)
        source?.let {
            target.name = SchemaFunction.propertyByName(source.property, Constants.NAME).value
            target.version = SchemaFunction.propertyByName(source.property, Constants.VERSION).value
            target.licenseeAutoCreate = java.lang.Boolean.parseBoolean(
                SchemaFunction.propertyByName(
                    source.property,
                    Constants.Product.LICENSEE_AUTO_CREATE, java.lang.Boolean.FALSE.toString()
                ).value
            )
            target.description = SchemaFunction.propertyByName(source.property, Constants.Product.DESCRIPTION)
                .value
            target.licensingInfo = SchemaFunction.propertyByName(source.property, Constants.Product.LICENSING_INFO)
                .value

            for (list in source.list) {
                if (Constants.DISCOUNT == list.name) {
                    val productDiscount = ProductDiscountImpl()
                    val price = convertPrice(list.property, Constants.Product.Discount.TOTAL_PRICE)
                    productDiscount.totalPrice = price.amount
                    productDiscount.currency = price.currencyCode
                    if (SchemaFunction.propertyByName(
                            list.property,
                            Constants.Product.Discount.AMOUNT_FIX
                        ).value != null
                    ) {
                        val amountFix = convertPrice(list.property, Constants.Product.Discount.AMOUNT_FIX)
                        productDiscount.amountFix = amountFix.amount
                    }
                    val amountPercent = SchemaFunction.propertyByName(
                        list.property,
                        Constants.Product.Discount.AMOUNT_PERCENT
                    ).value
                    if (amountPercent != null) {
                        try {
                            productDiscount.amountPercent = DatatypeConverter.parseDecimal(amountPercent)
                        } catch (e: NumberFormatException) {
                            throw IllegalArgumentException(
                                "Format for discount amount in percent is not correct, expected numeric format"
                            )
                        }
                    }
                    target.addDiscount(productDiscount)
                }
            }
            Collections.sort<ProductDiscount>(target.productDiscounts!!)

            // Custom properties
            for (property in source.property) {
                if (!ProductImpl.reservedProps.contains(property.name)) {
                    target.addProperty(property.name, property.value)
                }
            }
        }
        return target
    }

    public override fun newTarget(): Product {
        return ProductImpl()
    }
}
