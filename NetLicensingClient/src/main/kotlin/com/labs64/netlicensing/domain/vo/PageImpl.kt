package com.labs64.netlicensing.domain.vo

import java.io.Serializable

/**
 * Basic `Page` implementation.
 *
 * @param <Entity>
 * the type of which the page consists.
</Entity> */
class PageImpl<Entity : Any>(
    content: List<Entity>?,
    override val pageNumber: Int?,
    override val itemsNumber: Int?,
    override val totalPages: Int?,
    override val totalItems: Long?,
    private val hasNext: Boolean?
) : Page<Entity>, Serializable {

    override val content = ArrayList<Entity>()

    init {
        assert(content != null) { "Content must not be null!" }

        this.content.addAll(content!!)
    }

    override fun hasNext(): Boolean? {
        return hasNext
    }

    override fun iterator(): Iterator<Entity> {
        return content.iterator()
    }

    override fun hasContent(): Boolean {
        return !content.isEmpty()
    }

    override fun toString(): String {
        var contentType = "UNKNOWN"

        if (hasContent()) {
            contentType = content[0].javaClass.getName()
        }

        return String.format("Page %s of %d containing %s instances", pageNumber, totalPages, contentType)
    }

    companion object {

        /**
         * Safe create instance of `Page`.
         *
         * @param content
         * the content of this page, must not be null.
         * @param pageNumber
         * the number of the current page
         * @param itemsNumber
         * the number of elements on the page
         * @param totalPages
         * the number of total pages
         * @param totalItems
         * the total amount of elements
         * @param hasNext
         * is there a next page exists
         * @param <E>
         * type of page entity
        </E> */
        fun <E : Any> createInstance(
            content: List<E>?,
            pageNumber: String?,
            itemsNumber: String?,
            totalPages: String?,
            totalItems: String?,
            hasNext: String?
        ): PageImpl<E> {
            try {
                return PageImpl(
                    content,
                    Integer.valueOf(pageNumber),
                    Integer.valueOf(itemsNumber),
                    Integer.valueOf(totalPages),
                    java.lang.Long.valueOf(totalItems),
                    java.lang.Boolean.valueOf(hasNext)
                )
            } catch (e: Exception) {
                return PageImpl(content, 0, 0, 0, 0, false)
            }
        }
    }
}
