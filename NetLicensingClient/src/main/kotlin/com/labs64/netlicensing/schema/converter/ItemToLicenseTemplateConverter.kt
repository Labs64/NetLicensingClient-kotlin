package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.LicenseTemplate
import com.labs64.netlicensing.domain.entity.impl.LicenseTemplateImpl
import com.labs64.netlicensing.domain.entity.impl.ProductModuleImpl
import com.labs64.netlicensing.domain.vo.Currency
import com.labs64.netlicensing.domain.vo.LicenseType
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item

class ItemToLicenseTemplateConverter : ItemToEntityBaseConverter<LicenseTemplate>() {

    @Throws(ConversionException::class)
    override fun convert(source: Item): LicenseTemplate {
        val target = super.convert(source)

        target.name = SchemaFunction.propertyByName(source.property, Constants.NAME).value
        target.licenseType = LicenseType.valueOf(
            SchemaFunction.propertyByName(
                source.property,
                Constants.LicenseTemplate.LICENSE_TYPE
            ).value
        )
        if (SchemaFunction.propertyByName(source.property, Constants.PRICE).value != null) {
            val price = convertPrice(source.property, Constants.PRICE)
            target.price = price.amount
            target.currency = Currency.valueOf(price.currencyCode!!)
        }
        target.automatic = java.lang.Boolean.parseBoolean(
            SchemaFunction.propertyByName(
                source.property,
                Constants.LicenseTemplate.AUTOMATIC,
                java.lang.Boolean.FALSE.toString()
            ).value
        )
        target.hidden = java.lang.Boolean.parseBoolean(
            SchemaFunction.propertyByName(
                source.property,
                Constants.LicenseTemplate.HIDDEN,
                java.lang.Boolean.FALSE.toString()
            ).value
        )
        target.hideLicenses = java.lang.Boolean.parseBoolean(
            SchemaFunction.propertyByName(
                source.property,
                Constants.LicenseTemplate.HIDE_LICENSES,
                java.lang.Boolean.FALSE.toString()
            ).value
        )

        // Custom properties
        for (property in source.property) {
            if (!LicenseTemplateImpl.reservedProps.contains(property.name)) {
                target.addProperty(property.name, property.value)
            }
        }

        target.productModule = ProductModuleImpl()
        target.productModule!!.number =
                SchemaFunction.propertyByName(source.property, Constants.ProductModule.PRODUCT_MODULE_NUMBER)
                    .value

        return target
    }

    public override fun newTarget(): LicenseTemplate {
        return LicenseTemplateImpl()
    }
}
