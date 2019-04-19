package com.labs64.netlicensing.domain.vo

interface Page<Entity> : Iterable<Entity> {

    /**
     * Returns the number of the current page. Is always non-negative.
     *
     * @return the number of the current page.
     */
    val pageNumber: Int?

    /**
     * Returns the number of elements on the page.
     *
     * @return the number of elements on the page.
     */
    val itemsNumber: Int?

    /**
     * Returns the number of total pages.
     *
     * @return the number of total pages
     */
    val totalPages: Int?

    /**
     * Returns the total amount of elements.
     *
     * @return the total amount of elements
     */
    val totalItems: Long?

    /**
     * Return container content.
     *
     * @return container content
     */
    val content: List<Entity>

    /**
     * Returns if there is a next page exists.
     *
     * @return true if there is a next page exists, otherwise false.
     */
    fun hasNext(): Boolean?

    /**
     * Returns if there is a content exists.
     *
     * @return true if there is a content exists, otherwise false
     */
    fun hasContent(): Boolean
}