package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.entity.PaymentMethod

class PaymentMethodImpl : BaseEntityImpl(), PaymentMethod {

    val paymentMethodProperties: Map<String, String>?
        get() = properties

    companion object {
        val reservedProps: List<String>
            get() = BaseEntityImpl.reservedProps
    }
}
