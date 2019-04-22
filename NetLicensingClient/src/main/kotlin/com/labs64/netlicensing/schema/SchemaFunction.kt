package com.labs64.netlicensing.schema

import com.labs64.netlicensing.schema.context.InfoEnum
import com.labs64.netlicensing.schema.context.Item
import com.labs64.netlicensing.schema.context.Netlicensing
import com.labs64.netlicensing.schema.context.ObjectFactory
import com.labs64.netlicensing.schema.context.Property
import java.io.UnsupportedEncodingException

object SchemaFunction {

    fun propertyByName(properties: List<Property>, name: String): Property {
        return propertyByName(properties, name, null)
    }

    fun propertyByName(properties: List<Property>, name: String, defaultValue: String?): Property {
        for (property in properties) {
            if (name.equals(property.name, ignoreCase = true)) {
                return property
            }
        }
        return Property(defaultValue, name)
    }

    fun propertyEncodedValueByName(properties: List<Property>, name: String): String? {
        val property = propertyByName(properties, name, null)
        try {
            return java.net.URLEncoder.encode(property.value, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            return property.value
        }
    }

    fun entityPropertyByName(properties: Map<String, String>, name: String): Property {
        for ((key, value) in properties) {
            if (name.equals(key, ignoreCase = true)) {
                return Property(value, name)
            }
        }
        return Property("", name)
    }

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

    fun findListByName(item: Item, listName: String): com.labs64.netlicensing.schema.context.List? {
        for (list in item.list) {
            if (listName == list.name) {
                return list
            }
        }
        return null
    }

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

    fun hasErrorInfos(entity: Netlicensing?): Boolean {
        return entity != null && entity.infos != null && !entity.infos.info.isEmpty()
    }

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