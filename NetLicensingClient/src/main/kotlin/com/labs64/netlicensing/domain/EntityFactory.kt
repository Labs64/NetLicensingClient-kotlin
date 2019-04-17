package com.labs64.netlicensing.domain

import com.labs64.netlicensing.domain.entity.Country
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.domain.vo.PageImpl
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.exception.WrongResponseFormatException
import com.labs64.netlicensing.schema.context.Item
import com.labs64.netlicensing.schema.context.Netlicensing
import com.labs64.netlicensing.schema.converter.Converter
import com.labs64.netlicensing.schema.converter.ItemToCountryConverter
import com.labs64.netlicensing.util.Visitable
import java.util.concurrent.ConcurrentHashMap

class EntityFactory {
    private val entityToConverterMap = HashMap<Class<*>, Class<*>>()

    init {
        entityToConverterMap[Country::class.java] = ItemToCountryConverter::class.java
    }

    private var convertersCache: MutableMap<Class<*>, Converter<Item, *>>? = ConcurrentHashMap()

//    private fun getConvertersCache(): MutableMap<Class<*>, Converter<Item, *>>? {
//        if (convertersCache == null) {
//            convertersCache = ConcurrentHashMap()
//        }
//        return convertersCache as MutableMap<Class<*>, Converter<Item, *>>
//    }

    /**
     * Creates page of entities of specified class from service response
     *
     * @param netlicensing
     * service XML response
     * @param entityClass
     * entity class
     * @return page of entities created from service response
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     */
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

    /**
     * Gets the entity class by response item type.
     *
     * @param item
     * the item
     * @return the entity class, if match is found
     * @throws WrongResponseFormatException
     * if match is not found
     */
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

    /**
     * Returns converter that is able to convert an [Item] object to an entity of specified class
     *
     * @param entityClass
     * entity class
     * @return [Converter] suitable for the given entity class
     */
    private fun <T> converterFor(entityClass: Class<T>): Converter<Item, T> {
        var converter: Converter<Item, T>? = null
        try {
            if (!convertersCache!!.isEmpty()) {
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