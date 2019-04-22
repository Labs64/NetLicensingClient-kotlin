package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.exception.ConversionException

interface Converter<S, T> {
    @Throws(ConversionException::class)
    fun convert(source: S?): T
}
