package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.entity.PaymentMethod

/**
 * Default implementation of [com.labs64.netlicensing.domain.entity.PaymentMethod].
 */
class PaymentMethodImpl : BaseEntityImpl(), PaymentMethod {

    val paymentMethodProperties: Map<String, String>
        get() = properties

    companion object {
        /**
         * @see BaseEntityImpl.getReservedProps
         */
        val reservedProps: List<String>
            get() = BaseEntityImpl.reservedProps
    }
}
