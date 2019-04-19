package com.labs64.netlicensing.domain.entity

interface ProductModule : BaseEntity {

    var name: String?

    var licensingModel: String?

    var product: Product?

    val licenseTemplates: MutableCollection<LicenseTemplate>?
}