package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Licensee
import com.labs64.netlicensing.domain.entity.impl.LicenseeImpl
import com.labs64.netlicensing.domain.entity.impl.ProductImpl
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item

class ItemToLicenseeConverter : ItemToEntityBaseConverter<Licensee>() {

    @Throws(ConversionException::class)
    override fun convert(source: Item?): Licensee {
        val target = super.convert(source)
        source?.let {
            // Custom properties
            for (property in source.property) {
                if (!LicenseeImpl.reservedProps.contains(property.name)) {
                    target.addProperty(property.name, property.value)
                }
            }

            target.product = ProductImpl()
            target.product!!.number =
                    SchemaFunction.propertyByName(source.property, Constants.Product.PRODUCT_NUMBER).value
        }

        return target
    }

    public override fun newTarget(): Licensee {
        return LicenseeImpl()
    }
}