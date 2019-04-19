package com.labs64.netlicensing.domain.entity

interface Licensee : BaseEntity {

    var product: Product?

    val licenses: MutableCollection<License>?
}