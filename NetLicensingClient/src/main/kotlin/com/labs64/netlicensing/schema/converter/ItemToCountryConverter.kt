package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Country
import com.labs64.netlicensing.domain.entity.impl.CountryImpl
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item
import javax.xml.bind.DatatypeConverter

class ItemToCountryConverter : ItemToEntityBaseConverter<Country>() {

    @Throws(ConversionException::class)
    override fun convert(source: Item?): Country {
        val target = super.convert(source)

        source?.let {
            if (SchemaFunction.propertyByName(source.property, Constants.Country.CODE).value != null) {
                target.code = SchemaFunction.propertyByName(source.property, Constants.Country.CODE).value
            }
            if (SchemaFunction.propertyByName(source.property, Constants.Country.NAME).value != null) {
                target.name = SchemaFunction.propertyByName(source.property, Constants.Country.NAME).value
            }
            if (SchemaFunction.propertyByName(source.property, Constants.Country.VAT_PERCENT).value != null) {
                target.vatPercent = DatatypeConverter.parseDecimal(
                    SchemaFunction.propertyByName(source.property, Constants.Country.VAT_PERCENT).value
                )
            }
            if (SchemaFunction.propertyByName(source.property, Constants.Country.IS_EU).value != null) {
                target.isEu = java.lang.Boolean
                    .valueOf(SchemaFunction.propertyByName(source.property, Constants.Country.IS_EU).value)
            }
        }
        return target
    }

    public override fun newTarget(): Country {
        return CountryImpl()
    }
}