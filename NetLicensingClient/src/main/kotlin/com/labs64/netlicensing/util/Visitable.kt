package com.labs64.netlicensing.util

open class Visitable {

    @Throws(Exception::class)
    fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}
