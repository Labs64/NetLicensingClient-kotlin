package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.License
import com.labs64.netlicensing.domain.entity.impl.LicenseImpl
import com.labs64.netlicensing.domain.entity.impl.LicenseTemplateImpl
import com.labs64.netlicensing.domain.entity.impl.LicenseeImpl
import com.labs64.netlicensing.domain.vo.Currency
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item
import javax.xml.bind.DatatypeConverter

/**
 * Convert [Item] entity into [License] object.
 */
class ItemToLicenseConverter : ItemToEntityBaseConverter<License>() {

    @Throws(ConversionException::class)
    override fun convert(source: Item): License {
        val target = super.convert(source)

        target.name = SchemaFunction.propertyByName(source.property, Constants.NAME).value
        if (SchemaFunction.propertyByName(source.property, Constants.PRICE).value != null) {
            target.price = DatatypeConverter.parseDecimal(
                SchemaFunction.propertyByName(
                    source.property,
                    Constants.PRICE
                ).value
            )
        }
        if (SchemaFunction.propertyByName(source.property, Constants.CURRENCY).value != null) {
            target.currency = Currency.valueOf(
                SchemaFunction.propertyByName(source.property, Constants.CURRENCY)
                    .value
            )
        }
        target.hidden = java.lang.Boolean.parseBoolean(
            SchemaFunction.propertyByName(
                source.property,
                Constants.License.HIDDEN, java.lang.Boolean.FALSE.toString()
            ).value
        )

        // Custom properties
        for (property in source.property) {
            if (!LicenseImpl.reservedProps.contains(property.name)) {
                target.addProperty(property.name, property.value)
            }
        }

        target.licensee = LicenseeImpl()
        target.licensee!!
            .number = SchemaFunction.propertyByName(source.property, Constants.Licensee.LICENSEE_NUMBER)
            .value

        target.licenseTemplate = LicenseTemplateImpl()
        target.licenseTemplate!!
            .number = SchemaFunction.propertyByName(
            source.property,
            Constants.LicenseTemplate.LICENSE_TEMPLATE_NUMBER
        )
            .value

        return target
    }

    public override fun newTarget(): License {
        return LicenseImpl()
    }
}