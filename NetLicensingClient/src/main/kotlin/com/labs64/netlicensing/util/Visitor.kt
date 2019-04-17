package com.labs64.netlicensing.util

import java.lang.reflect.Method

open class Visitor {

    @Throws(Exception::class)
    fun visit(any: Any) {
        val method = getMethod(any.javaClass)
        method.invoke(this, any)
    }

    fun visitDefault(any: Any) {
        // Do nothing
    }

    @Throws(NoSuchMethodException::class)
    protected fun getMethod(targetClass: Class<*>): Method {
        var superClass = targetClass
        while (superClass != Any::class.java) {
            try {
                return javaClass.getMethod("visit", superClass)
            } catch (e: NoSuchMethodException) {
                superClass = superClass.superclass
            }
        }
        val ifaces = targetClass.interfaces
        for (iface in ifaces) {
            try {
                return javaClass.getMethod("visit", iface)
            } catch (e: NoSuchMethodException) {
            }
        }
        return javaClass.getMethod("visitDefault", Any::class.java)
    }
}