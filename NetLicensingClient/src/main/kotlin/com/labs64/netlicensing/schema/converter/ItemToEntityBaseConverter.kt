package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.BaseEntity
import com.labs64.netlicensing.domain.vo.Money
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item
import com.labs64.netlicensing.schema.context.Property

abstract class ItemToEntityBaseConverter<T : BaseEntity> : Converter<Item, T> {

    protected abstract fun newTarget(): T

    @Throws(ConversionException::class)
    override fun convert(source: Item?): T {
        val target = newTarget()

        source?.let {
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
        }

        return target
    }

    fun convertPrice(source: List<Property>, priceProperty: String): Money {
        val rawPrice = SchemaFunction.propertyByName(source, priceProperty).getValue()
        val rawCurrency = SchemaFunction.propertyByName(source, Constants.CURRENCY).getValue()
        return Money.convertPrice(rawPrice, rawCurrency)
    }
}