package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.vo.LicensingModelProperties
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item

class ItemToLicensingModelPropertiesConverter : Converter<Item, LicensingModelProperties> {

    @Throws(ConversionException::class)
    override fun convert(source: Item?): LicensingModelProperties {
        source?.let {
            if (Constants.Utility.LICENSING_MODEL_PROPERTIES != source.type) {
                val sourceType = if (source.type != null) source.type else "<null>"
                throw ConversionException(
                    String.format(
                        "Wrong item type '%s', expected '%s'", sourceType,
                        Constants.Utility.LICENSING_MODEL_PROPERTIES
                    )
                )
            }
        }

        val name = SchemaFunction.propertyByName(source?.property!!, Constants.NAME).value
        return LicensingModelPropertiesImpl(name)
    }

    private class LicensingModelPropertiesImpl(override val name: String) : LicensingModelProperties
}