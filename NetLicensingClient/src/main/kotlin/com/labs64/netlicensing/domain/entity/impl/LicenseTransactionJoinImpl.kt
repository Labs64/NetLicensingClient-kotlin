package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.entity.License
import com.labs64.netlicensing.domain.entity.LicenseTransactionJoin
import com.labs64.netlicensing.domain.entity.Transaction

class LicenseTransactionJoinImpl : LicenseTransactionJoin {

    override var transaction: Transaction? = null
    override var license: License? = null
}