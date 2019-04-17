package com.labs64.netlicensing.schema

import com.labs64.netlicensing.schema.context.InfoEnum
import com.labs64.netlicensing.schema.context.Item
import com.labs64.netlicensing.schema.context.Netlicensing
import com.labs64.netlicensing.schema.context.Property
import com.labs64.netlicensing.schema.context.ObjectFactory
import java.io.UnsupportedEncodingException

object SchemaFunction {

    /**
     * Get [Property] by name. Property name is not case-sensitive!
     *
     * @param properties
     * properties collection
     * @param name
     * property name
     * @return [Property] object or "null"-property (w/o value) if no property with the given name is present
     */
    fun propertyByName(properties: List<Property>, name: String): Property {
        return propertyByName(properties, name, null)
    }

    /**
     * Get [Property] by name. Property name is not case-sensitive! If property with the given name is not found,
     * a property with provided default value is returned.
     *
     * @param properties
     * properties collection
     * @param name
     * property name
     * @param defaultValue
     * default value to be used if no property found
     * @return [Property] object with found or default value
     */
    fun propertyByName(properties: List<Property>, name: String, defaultValue: String?): Property {
        for (property in properties) {
            if (name.equals(property.name, ignoreCase = true)) {
                return property
            }
        }
        return Property(defaultValue, name)
    }

    /**
     * Get url-encoded Property value by name. Property name is not case-sensitive!
     *
     * @param properties
     * properties collection
     * @param name
     * property name
     * @return {String} value or "null"-String if no property with the given name is present
     */
    fun propertyEncodedValueByName(properties: List<Property>, name: String): String? {
        val property = propertyByName(properties, name, null)
        if (property != null) {
            try {
                return java.net.URLEncoder.encode(property.value, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                return property.value
            }
        }
        return null
    }

    /**
     * Get [Property] derivative by name, converting it to [Property]. Property name is not case-sensitive!
     * If property with the given name is not found, a property with empty value is returned.
     *
     * @param properties
     * properties collection
     * @param name
     * property name
     * @return [Property] object with found or empty value
     */
    fun entityPropertyByName(properties: Map<String, String>, name: String): Property {
        for ((key, value) in properties) {
            if (name.equals(key, ignoreCase = true)) {
                return Property(value, name)
            }
        }
        return Property("", name)
    }

    /**
     * Get [Item] by existing property value. Comparison is not case-sensitive!
     *
     * @param items
     * items collection
     * @param propertyName
     * property name
     * @param propertyValue
     * property value
     * @return [Item] object or "null" if no property with the given name is present
     */
    fun findItemByProperty(
        items: Netlicensing.Items,
        propertyName: String,
        propertyValue: String?
    ): Item? {
        if (propertyValue != null) {
            for (item in items.item) {
                val value = propertyByName(item.property, propertyName).value
                if (propertyValue.equals(value, ignoreCase = true)) {
                    return item
                }
            }
        }
        return null
    }

    /**
     * Get [com.labs64.netlicensing.schema.context.List] by name from [Item].
     *
     * @param item
     * item containing (multiple) lists
     * @param listName
     * the value of the list "name" attribute
     * @return [com.labs64.netlicensing.schema.context.List] object or "null" if no list with the given name is
     * present
     */
    fun findListByName(item: Item, listName: String): com.labs64.netlicensing.schema.context.List? {
        for (list in item.list) {
            if (listName == list.name) {
                return list
            }
        }
        return null
    }

    /**
     * Creates and adds [Info] object to [Netlicensing] object
     *
     * @param netlicensing
     * [Netlicensing] object
     * @param id
     * [Info] object identifier
     * @param type
     * [Info] object type
     * @param value
     * [Info] object value
     */
    fun addInfo(
        netlicensing: Netlicensing,
        id: String,
        type: InfoEnum,
        value: String
    ) {
        val objectFactory = ObjectFactory()

        if (netlicensing.infos == null) {
            netlicensing.infos = objectFactory.createNetlicensingInfos()
        }

        val info = objectFactory.createInfo()
        info.id = id
        info.type = type
        info.value = value
        netlicensing.infos.info.add(info)
    }

    /**
     * Check if [Netlicensing] object contains service errors.
     *
     * @param entity
     * [Netlicensing] object to be checked
     * @return true if any error have been found, otherwise false
     */
    fun hasErrorInfos(entity: Netlicensing?): Boolean {
        return entity != null && entity.infos != null && !entity.infos.info.isEmpty()
    }

    /**
     * Transform service infos to a string message.
     *
     * @param response
     * service response (containing the infos)
     * @return message string
     */
    fun infosToMessage(response: Netlicensing?): String {
        val errorMessages = StringBuilder()
        if (hasErrorInfos(response)) {
            if (response != null) {
                for (info in response.infos.info) {
                    if (errorMessages.length > 0) {
                        errorMessages.append(", ")
                    }
                    errorMessages.append(info.id).append(": ").append(info.value.substring(0, 1).toUpperCase())
                        .append(info.value.substring(1))
                }
            }
        } else {
            errorMessages.append("Infos not provided with the service response.")
        }
        return errorMessages.toString()
    }
}