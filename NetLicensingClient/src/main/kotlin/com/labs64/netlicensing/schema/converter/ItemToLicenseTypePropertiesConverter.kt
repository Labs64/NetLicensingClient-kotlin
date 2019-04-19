package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.vo.LicenseTypeProperties
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item

/**
 * Converts [Item] entity into an implementation of [LicenseTypeProperties] interface.
 */
class ItemToLicenseTypePropertiesConverter : Converter<Item, LicenseTypeProperties> {

    @Throws(ConversionException::class)
    override fun convert(source: Item): LicenseTypeProperties {
        if (Constants.Utility.LICENSE_TYPE != source.type) {
            val sourceType = if (source.type != null) source.type else "<null>"
            throw ConversionException(
                String.format(
                    "Wrong item type '%s', expected '%s'", sourceType,
                    Constants.Utility.LICENSE_TYPE
                )
            )
        }

        val name = SchemaFunction.propertyByName(source.property, Constants.NAME).value
        return LicenseTypePropertiesImpl(name)
    }

    private class LicenseTypePropertiesImpl(override val name: String) : LicenseTypeProperties
}
