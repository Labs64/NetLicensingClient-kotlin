package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.vo.Composition
import com.labs64.netlicensing.domain.vo.ValidationResult
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.context.List
import com.labs64.netlicensing.schema.context.Netlicensing
import com.labs64.netlicensing.util.DateUtils

class ItemsToValidationResultConverter : Converter<Netlicensing, ValidationResult> {

    @Throws(ConversionException::class)
    override fun convert(source: Netlicensing?): ValidationResult {
        val target = ValidationResult()
        if (source == null) {
            return target
        }

        if (source.ttl != null) {
            target.ttl = DateUtils.parseDate(source.ttl.toXMLFormat())
        }

        if (source.items == null) {
            return target
        }

        for (item in source.items.item) {
            if (Constants.ValidationResult.VALIDATION_RESULT_TYPE != item.type) {
                continue
            }

            val composition = Composition()

            // convert properties
            var productModuleNumber: String? = null
            for (property in item.property) {
                if (Constants.ProductModule.PRODUCT_MODULE_NUMBER == property.name) {
                    productModuleNumber = property.value
                } else {
                    composition.put(property.name, property.value)
                }
            }

            // convert lists
            if (item.list != null) {
                for (list in item.list) {
                    composition.put(list.name, convertFromList(list))
                }
            }

            if (productModuleNumber == null) {
                throw ConversionException(
                    String.format(
                        "Validation item does not contain property '%s'",
                        Constants.ProductModule.PRODUCT_MODULE_NUMBER
                    )
                )
            }

            target.setProductModuleValidation(productModuleNumber, composition)
        }
        return target
    }

    private fun convertFromList(list: List): Composition {
        val composition = Composition()
        // convert properties
        if (list.property != null) {
            for (property in list.property) {
                composition.put(property.name, property.value)
            }
        }
        // convert lists
        if (list.list != null) {
            for (sublist in list.list) {
                composition.put(list.name, convertFromList(sublist))
            }
        }
        return composition
    }
}