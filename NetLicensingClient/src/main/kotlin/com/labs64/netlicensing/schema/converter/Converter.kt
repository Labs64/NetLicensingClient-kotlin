package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.exception.ConversionException

/**
 * A converter converts a source object of type S to a target of type T. Implementations of this interface are
 * thread-safe and can be shared.
 *
 * @param <S>
 * The source type
 * @param <T>
 * The target type
</T></S> */
interface Converter<S, T> {

    /**
     * Convert the source of type S to target type T.
     *
     * @param source
     * the source object to converter, which must be an instance of S
     * @return the converted object, which must be an instance of T
     * @throws ConversionException
     * if the source could not be converted to the desired target type
     */
    @Throws(ConversionException::class)
    fun convert(source: S): T
}
