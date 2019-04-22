package com.labs64.netlicensing.domain

import com.labs64.netlicensing.domain.entity.Country
import com.labs64.netlicensing.domain.entity.License
import com.labs64.netlicensing.domain.entity.LicenseTemplate
import com.labs64.netlicensing.domain.entity.Licensee
import com.labs64.netlicensing.domain.entity.PaymentMethod
import com.labs64.netlicensing.domain.entity.Product
import com.labs64.netlicensing.domain.entity.ProductModule
import com.labs64.netlicensing.domain.entity.Token
import com.labs64.netlicensing.domain.entity.Transaction
import com.labs64.netlicensing.domain.vo.LicenseTypeProperties
import com.labs64.netlicensing.domain.vo.LicensingModelProperties
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.domain.vo.PageImpl
import com.labs64.netlicensing.domain.vo.ValidationResult
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.exception.WrongResponseFormatException
import com.labs64.netlicensing.schema.context.Item
import com.labs64.netlicensing.schema.context.Netlicensing
import com.labs64.netlicensing.schema.converter.Converter
import com.labs64.netlicensing.schema.converter.ItemToCountryConverter
import com.labs64.netlicensing.schema.converter.ItemToLicenseConverter
import com.labs64.netlicensing.schema.converter.ItemToLicenseTemplateConverter
import com.labs64.netlicensing.schema.converter.ItemToLicenseTypePropertiesConverter
import com.labs64.netlicensing.schema.converter.ItemToLicenseeConverter
import com.labs64.netlicensing.schema.converter.ItemToLicensingModelPropertiesConverter
import com.labs64.netlicensing.schema.converter.ItemToPaymentMethodConverter
import com.labs64.netlicensing.schema.converter.ItemToProductConverter
import com.labs64.netlicensing.schema.converter.ItemToProductModuleConverter
import com.labs64.netlicensing.schema.converter.ItemToTokenConverter
import com.labs64.netlicensing.schema.converter.ItemToTransactionConverter
import com.labs64.netlicensing.schema.converter.ItemsToValidationResultConverter
import com.labs64.netlicensing.util.Visitable
import java.util.concurrent.ConcurrentHashMap

class EntityFactory {
    private val entityToConverterMap = HashMap<Class<*>, Class<*>>()

    init {
        entityToConverterMap[Country::class.java] = ItemToCountryConverter::class.java
        entityToConverterMap[LicenseTypeProperties::class.java] = ItemToLicenseTypePropertiesConverter::class.java
        entityToConverterMap[LicensingModelProperties::class.java] = ItemToLicensingModelPropertiesConverter::class.java
        entityToConverterMap[Product::class.java] = ItemToProductConverter::class.java
        entityToConverterMap[ProductModule::class.java] = ItemToProductModuleConverter::class.java
        entityToConverterMap[LicenseTemplate::class.java] = ItemToLicenseTemplateConverter::class.java
        entityToConverterMap[Licensee::class.java] = ItemToLicenseeConverter::class.java
        entityToConverterMap[License::class.java] = ItemToLicenseConverter::class.java
        entityToConverterMap[PaymentMethod::class.java] = ItemToPaymentMethodConverter::class.java
        entityToConverterMap[Token::class.java] = ItemToTokenConverter::class.java
        entityToConverterMap[Transaction::class.java] = ItemToTransactionConverter::class.java
    }

    private var convertersCache: MutableMap<Class<*>, Converter<Item, *>>? = ConcurrentHashMap()

    @Throws(NetLicensingException::class)
    fun <T : Any> createPage(netlicensing: Netlicensing?, entityClass: Class<T>): Page<T> {
        if (netlicensing?.items != null) {
            val entities = ArrayList<T>()
            val linkedEntities = ArrayList<Any>()

            for (item in netlicensing.items.item) {
                val itemEntityClass = getEntityClassByItemType(item)
                if (entityClass.isAssignableFrom(itemEntityClass)) {
                    entities.add(converterFor(entityClass).convert(item))
                } else {
                    linkedEntities.add(converterFor(itemEntityClass).convert(item))
                }
            }

            if (!linkedEntities.isEmpty()) {
                for (entity in entities) {
                    if (Visitable::class.java.isAssignableFrom(entity.javaClass)) {
                        try {
                            (entity as Visitable).accept(LinkedEntitiesPopulator(linkedEntities))
                        } catch (e: Exception) {
                            throw ConversionException("Error processing linked entities")
                        }
                    }
                }
            }

            return PageImpl.createInstance(
                entities,
                netlicensing.items.pagenumber,
                netlicensing.items.itemsnumber,
                netlicensing.items.totalpages,
                netlicensing.items.totalitems,
                netlicensing.items.hasnext
            )
        } else {
            throw WrongResponseFormatException("Service response is not a page response")
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(NetLicensingException::class)
    fun <T> create(netlicensing: Netlicensing, entityClass: Class<T>): T {
        if (entityClass == ValidationResult::class.java) {
            return ItemsToValidationResultConverter().convert(netlicensing) as T
        } else {
            val item = findSuitableItemOfType(netlicensing, entityClass)
            return converterFor(entityClass).convert(item)
        }
    }

    @Throws(WrongResponseFormatException::class)
    private fun findSuitableItemOfType(netlicensing: Netlicensing, type: Class<*>): Item {
        if (netlicensing.items != null) {
            for (item in netlicensing.items.item) {
                if (isItemOfType(item, type)) {
                    return item
                }
            }
        }
        throw WrongResponseFormatException("Service response doesn't contain item of type " + type.canonicalName)
    }

    private fun isItemOfType(item: Item, type: Class<*>): Boolean {
        return type.simpleName == item.type || type.simpleName == item.type + "Properties"
    }

    @Throws(WrongResponseFormatException::class)
    private fun getEntityClassByItemType(item: Item): Class<*> {
        val itemType = item.type
        val itemTypeProps = itemType + "Properties"
        for (entityClass in entityToConverterMap.keys) {
            if (entityClass.simpleName == itemType || entityClass.simpleName == itemTypeProps) {
                return entityClass
            }
        }
        throw WrongResponseFormatException("Service response contains unexpected item type $itemType")
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> converterFor(entityClass: Class<T>): Converter<Item, T> {
        var converter: Converter<Item, T>? = null
        try {
            if (!convertersCache!!.isEmpty() && convertersCache!!.get(entityClass) != null) {
                converter = convertersCache.let { convertersCache!!.get(entityClass) as Converter<Item, T> }
            }
        } catch (e: ClassCastException) {
            throw RuntimeException("Wrong converter type found for entity class " + entityClass.canonicalName)
        }

        if (converter == null) {
            val converterClass = entityToConverterMap[entityClass]
                ?: throw IllegalArgumentException("No converter is found for entity of class " + entityClass.canonicalName)
            try {
                converter = converterClass.newInstance() as Converter<Item, T>
                convertersCache!!.put(entityClass, converter)
            } catch (e: InstantiationException) {
                throw RuntimeException("Can not instantiate converter of class " + converterClass.canonicalName)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Can not instantiate converter of class " + converterClass.canonicalName)
            }
        }
        return converter
    }
}