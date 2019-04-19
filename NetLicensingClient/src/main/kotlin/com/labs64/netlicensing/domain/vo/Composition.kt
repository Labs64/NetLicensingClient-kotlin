package com.labs64.netlicensing.domain.vo

import java.io.Serializable
import java.util.HashMap

class Composition : Serializable {
    private var properties: MutableMap<String, Composition>? = null
        get() {
            if (field == null) {
                this.properties = HashMap()
            }
            return field
        }

    var value: String? = null

    constructor() { // list
        value = null
    }

    constructor(value: String) { // property value
        this.value = value
    }

    fun put(key: String, value: String) {
        properties!![key] = Composition(value)
    }

    fun put(key: String, value: Composition) {
        properties!![key] = value
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (value == null) {
            sb.append("{")
            if (properties == null) {
                sb.append("<null>")
            } else {
                var first = true
                for ((key, value1) in properties!!) {
                    if (first) {
                        first = false
                    } else {
                        sb.append(", ")
                    }
                    sb.append(key).append("=").append(value1)
                }
            }
            sb.append("}")
        } else {
            sb.append(value)
        }
        return sb.toString()
    }
}