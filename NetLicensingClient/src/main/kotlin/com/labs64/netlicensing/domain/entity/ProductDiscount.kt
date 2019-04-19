package com.labs64.netlicensing.domain.entity

import java.math.BigDecimal

interface ProductDiscount : Comparable<ProductDiscount> {

    var product: Product?

    var totalPrice: BigDecimal?

    var currency: String?

    var amountFix: BigDecimal?

    var amountPercent: BigDecimal?
}
