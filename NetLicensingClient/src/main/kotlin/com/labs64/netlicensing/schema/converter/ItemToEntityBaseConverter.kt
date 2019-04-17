package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.BaseEntity
import com.labs64.netlicensing.domain.vo.Money
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item
import com.labs64.netlicensing.schema.context.Property

/**
 * Convert [Item] entity into [BaseEntity] object.
 */
abstract class ItemToEntityBaseConverter<T : BaseEntity> : Converter<Item, T> {

    protected abstract fun newTarget(): T

    @Throws(ConversionException::class)
    override fun convert(source: Item): T {
        val target = newTarget()

        val entityClass = target.javaClass.interfaces[0].simpleName
        if (entityClass != source.type) {
            val sourceType = if (source.type != null) source.type else "<null>"
            throw ConversionException(String.format("Wrong item type '%s', expected '%s'", sourceType, entityClass))
        }

        target.active = java.lang.Boolean.parseBoolean(
            SchemaFunction.propertyByName(source.property, Constants.ACTIVE, java.lang.Boolean.FALSE.toString())
                .value
        )
        target.number = SchemaFunction.propertyByName(source.property, Constants.NUMBER).value

        return target
    }

    /**
     * Converts price with currency from NetLicensing XML representation to the internal Money value.
     *
     * @param source
     * - collection of properties from NetLicensing XML
     * @param priceProperty
     * - the property name holding price value, currency always assumed in Constants.CURRENCY
     * @return converted money object
     */
    fun convertPrice(source: List<Property>, priceProperty: String): Money {
        val rawPrice = SchemaFunction.propertyByName(source, priceProperty).getValue()
        val rawCurrency = SchemaFunction.propertyByName(source, Constants.CURRENCY).getValue()
        return Money.convertPrice(rawPrice, rawCurrency)
    }
}