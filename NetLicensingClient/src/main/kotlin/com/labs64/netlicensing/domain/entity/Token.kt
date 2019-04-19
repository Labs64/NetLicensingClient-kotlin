package com.labs64.netlicensing.domain.entity

import com.labs64.netlicensing.domain.vo.TokenType
import java.util.Date

interface Token : BaseEntity {

    var vendorNumber: String?

    var expirationTime: Date?

    var tokenType: TokenType?
}
