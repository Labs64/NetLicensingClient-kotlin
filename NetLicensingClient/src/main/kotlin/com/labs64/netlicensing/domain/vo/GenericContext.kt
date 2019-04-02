package com.labs64.netlicensing.domain.vo

import java.util.concurrent.ConcurrentHashMap

open class GenericContext<T : Any>(val valueClass: Class<T>) {

    private var contextMap: MutableMap<String, Any> = ConcurrentHashMap()

    protected fun getContextMap(): MutableMap<String, Any> {
        return contextMap
    }

    fun containsKey(key: String): Boolean {
        return getContextMap().containsKey(key)
    }

    fun containsKey(key: Class<*>): Boolean {
        return getContextMap().containsKey(key.name)
    }

    fun setValue(key: String, value: T?): GenericContext<T> {
        if (value != null) {
            getContextMap().put(key, value)
        }
        return this
    }

    fun setValue(key: Class<*>, value: T?): GenericContext<T> {
        return if (value == null) {
            this
        } else {
            setValue(key.name, value)
        }
    }

    fun setValue(value: T?): GenericContext<T> {
        return if (value == null) {
            this
        } else {
            setValue(value.javaClass, value)
        }
    }

    fun getValue(key: String): T? {
        val value = getContextMap()[key]
        return if (value != null && valueClass.isAssignableFrom(value.javaClass)) {
            valueClass.cast(value)
        } else {
            null
        }
    }

    fun getValue(key: Class<*>): T? {
        return getValue(key.name)
    }

    fun setObject(key: String, value: Any?): GenericContext<T> {
        if (value != null) {
            getContextMap()[key] = value
        }
        return this
    }

    fun setObject(key: Class<*>, value: Any?): GenericContext<T> {
        return if (value == null) {
            this
        } else {
            setObject(key.name, value)
        }
    }

    fun setObject(value: Any?): GenericContext<T> {
        return if (value == null) {
            this
        } else {
            setObject(value.javaClass, value)
        }
    }

    fun getObject(key: String): Any? {
        return getContextMap()[key]
    }

    fun getObject(key: Class<*>): Any? {
        return getObject(key.name)
    }
}