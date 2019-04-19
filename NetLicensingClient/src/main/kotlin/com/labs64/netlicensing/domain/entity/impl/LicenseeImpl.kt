package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.License
import com.labs64.netlicensing.domain.entity.Licensee
import com.labs64.netlicensing.domain.entity.Product
import java.util.ArrayList

class LicenseeImpl : BaseEntityImpl(), Licensee {

    override var product: Product? = null
        set(product) {
            product?.licensees!!.add(this)
            field = product
        }

    override var licenses: MutableCollection<License>? = null
        get() {
            if (field == null) {
                this.licenses = ArrayList()
            }
            return field
        }

    val licenseeProperties: Map<String, String>
        get() = properties

    companion object {
        /**
         * @see BaseEntityImpl.getReservedProps
         */
        // maps to 'product'
        // used by shop, therefore disallowed for user
        val reservedProps: List<String>
            get() {
                val reserved = BaseEntityImpl.reservedProps
                reserved.add(Constants.Product.PRODUCT_NUMBER)
                reserved.add(Constants.IN_USE)
                reserved.add(Constants.Vendor.VENDOR_NUMBER)
                return reserved
            }
    }
}