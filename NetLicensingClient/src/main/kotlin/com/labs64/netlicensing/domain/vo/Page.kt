package com.labs64.netlicensing.domain.vo

interface Page<Entity> : Iterable<Entity> {

    val pageNumber: Int?

    val itemsNumber: Int?

    val totalPages: Int?

    val totalItems: Long?

    val content: List<Entity>

    fun hasNext(): Boolean?

    fun hasContent(): Boolean
}