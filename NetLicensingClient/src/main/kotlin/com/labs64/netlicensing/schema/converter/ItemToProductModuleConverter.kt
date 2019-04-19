package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.ProductModule
import com.labs64.netlicensing.domain.entity.impl.ProductImpl
import com.labs64.netlicensing.domain.entity.impl.ProductModuleImpl
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item

class ItemToProductModuleConverter : ItemToEntityBaseConverter<ProductModule>() {

    @Throws(ConversionException::class)
    override fun convert(source: Item): ProductModule {
        val target = super.convert(source)

        target.name = SchemaFunction.propertyByName(source.property, Constants.NAME).value
        target.licensingModel = SchemaFunction.propertyByName(
            source.property,
            Constants.ProductModule.LICENSING_MODEL
        ).value

        target.product = ProductImpl()
        target.product!!.number = SchemaFunction.propertyByName(source.property, Constants.Product.PRODUCT_NUMBER).value

        // Custom properties
        for (property in source.property) {
            if (!ProductModuleImpl.reservedProps.contains(property.name)) {
                target.addProperty(property.name, property.value)
            }
        }

        return target
    }

    public override fun newTarget(): ProductModule {
        return ProductModuleImpl()
    }
}