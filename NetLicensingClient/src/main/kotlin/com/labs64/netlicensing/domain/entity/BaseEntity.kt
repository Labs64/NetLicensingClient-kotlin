package com.labs64.netlicensing.domain.entity

import java.io.Serializable
import javax.ws.rs.core.Form

/**
 * Defines properties common to all (or most) of other entities.
 */
interface BaseEntity : Serializable {

    // Methods for working with properties

    var number: String?

    var active: Boolean?

    // Methods for working with custom properties

    val properties: Map<String, String>?

    fun addProperty(property: String, value: String)

    fun removeProperty(property: String?)

    /**
     * Converts properties of the entity to the body of POST request
     *
     * @return object that represents HTML form data request encoded using the "application/x-www-form-urlencoded"
     * content type
     */
    fun asRequestForm(): Form?
}