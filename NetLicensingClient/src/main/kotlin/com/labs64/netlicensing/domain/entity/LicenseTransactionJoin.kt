package com.labs64.netlicensing.domain.entity

import java.io.Serializable

interface LicenseTransactionJoin : Serializable {

    var transaction: Transaction?

    var license: License?
}
