package com.labs64.netlicensing.domain.entity

import java.io.Serializable
import javax.ws.rs.core.Form

interface BaseEntity : Serializable {

    var number: String?

    var active: Boolean?

    val properties: MutableMap<String, String>?

    fun addProperty(property: String, value: String)

    fun removeProperty(property: String?)

    fun asRequestForm(): Form
}