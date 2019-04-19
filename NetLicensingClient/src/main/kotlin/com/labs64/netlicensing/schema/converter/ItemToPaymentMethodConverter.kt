package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.entity.PaymentMethod
import com.labs64.netlicensing.domain.entity.impl.PaymentMethodImpl
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.context.Item

/**
 * Convert [Item] entity into [PaymentMethod] object.
 */
class ItemToPaymentMethodConverter : ItemToEntityBaseConverter<PaymentMethod>() {

    @Throws(ConversionException::class)
    override fun convert(source: Item): PaymentMethod {
        val target = super.convert(source)

        // Custom properties
        for (property in source.property) {
            if (!PaymentMethodImpl.reservedProps.contains(property.name)) {
                target.addProperty(property.name, property.value)
            }
        }

        return target
    }

    public override fun newTarget(): PaymentMethod {
        return PaymentMethodImpl()
    }
}
