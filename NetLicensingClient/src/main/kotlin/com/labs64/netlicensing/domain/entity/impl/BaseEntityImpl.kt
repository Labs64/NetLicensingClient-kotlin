package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.BaseEntity
import com.labs64.netlicensing.util.Visitable
import java.util.concurrent.ConcurrentHashMap
import javax.ws.rs.core.Form
import javax.ws.rs.core.MultivaluedHashMap
import javax.ws.rs.core.MultivaluedMap

abstract class BaseEntityImpl : Visitable(), BaseEntity {

    override var number: String? = null

    override var active: Boolean? = null

    override var properties: MutableMap<String, String>? = null
        get() {
            if (field == null) {
                this.properties = ConcurrentHashMap()
            }
            return field
        }

    override fun addProperty(property: String, value: String) {
        properties?.set(property, value)
    }

    override fun removeProperty(property: String?) {
        properties?.remove(property)
    }

    override fun toString(): String {
        return toString(asPropertiesMap())
    }

    override fun asRequestForm(): Form {
        val form = Form()
        for ((key, value1) in asPropertiesMap()) {
            for (value in value1) {
                if (value != null) {
                    form.param(key, value.toString())
                }
            }
        }
        return form
    }

    protected open fun asPropertiesMap(): MultivaluedMap<String, Any> {
        val map = MultivaluedHashMap<String, Any>()
        map.add(Constants.NUMBER, number)
        map.add(Constants.ACTIVE, active)
        if (properties != null) {
            for ((key, value) in properties!!) {
                map.add(key, value)
            }
        }
        return map
    }

    protected fun toString(propMap: MultivaluedMap<String, Any>): String {
        val builder = StringBuilder(this.javaClass.simpleName)
        builder.append(" [")
        var firstProp = true
        for (propKey in propMap.keys) {
            val propValue = propMap[propKey]
            if (propValue != null && (propValue !is Collection<*> || (propValue as Collection<*>).size > 0)) {
                builder.append(if (firstProp) "" else ", ")
                firstProp = false

                builder.append(propKey).append("=")
                if (propValue is Collection<*>) {
                    builder.append(propValue.toString())
                } else {
                    val propValueStr = propValue.toString()
                    builder.append(if (propValueStr.length > 50) propValueStr.substring(0, 50) else propValue)
                }
            }
        }
        return builder.append("]").toString()
    }

    companion object {
        val reservedProps: MutableList<String>
            get() {
                val reserved = ArrayList<String>()
                reserved.add(Constants.ID)
                reserved.add(Constants.NUMBER)
                reserved.add(Constants.ACTIVE)
                reserved.add(Constants.DELETED)
                return reserved
            }
    }
}
