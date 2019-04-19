package com.labs64.netlicensing.domain.entity

interface Product : BaseEntity {

    var name: String?

    var version: String?

    var licenseeAutoCreate: Boolean?

    var description: String?

    var licensingInfo: String?

    val productModules: MutableCollection<ProductModule>?

    val licensees: MutableCollection<Licensee>?

    val productDiscounts: List<ProductDiscount>?

    fun addDiscount(discount: ProductDiscount)
}
