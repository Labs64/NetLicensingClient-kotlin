package com.labs64.netlicensing.domain.entity

import java.math.BigDecimal

interface Country : BaseEntity {

    var code: String?

    var name: String?

    var vatPercent: BigDecimal?

    var isEu: Boolean?
}
