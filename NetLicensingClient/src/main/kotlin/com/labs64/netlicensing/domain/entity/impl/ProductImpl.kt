package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Licensee
import com.labs64.netlicensing.domain.entity.Product
import com.labs64.netlicensing.domain.entity.ProductDiscount
import com.labs64.netlicensing.domain.entity.ProductModule
import javax.ws.rs.core.MultivaluedMap

class ProductImpl : BaseEntityImpl(), Product {

    override var name: String? = null

    override var version: String? = null

    override var licenseeAutoCreate: Boolean? = null

    override var description: String? = null

    override var licensingInfo: String? = null

    override var productModules: MutableCollection<ProductModule>? = null
        get() {
            if (field == null) {
                this.productModules = ArrayList()
            }
            return field
        }

    override var licensees: MutableCollection<Licensee>? = null
        get() {
            if (field == null) {
                this.licensees = ArrayList()
            }
            return field
        }

    private var productDiscountsTouched: Boolean? = false

    override var productDiscounts: MutableList<ProductDiscount>? = null
        get() {
            if (field == null) {
                this.productDiscounts = ArrayList()
            }
            return field
        }
        set(productDiscounts: MutableList<ProductDiscount>?) {
            field = productDiscounts
            for (productDiscount in field!!) {
                productDiscount.product = this
            }
            productDiscountsTouched = true
        }

    override fun addDiscount(discount: ProductDiscount) {
        discount.product = this
        productDiscounts?.add(discount)
        productDiscountsTouched = true
    }

    override fun asPropertiesMap(): MultivaluedMap<String, Any> {
        val map = super.asPropertiesMap()
        map.add(Constants.NAME, name)
        map.add(Constants.VERSION, version)
        map.add(Constants.Product.LICENSEE_AUTO_CREATE, licenseeAutoCreate)
        map.add(Constants.Product.DESCRIPTION, description)
        map.add(Constants.Product.LICENSING_INFO, licensingInfo)
        if (productDiscounts != null) {
            for (productDiscount in productDiscounts!!) {
                map.add(Constants.DISCOUNT, productDiscount.toString())
            }
        }

        if (map[Constants.DISCOUNT] == null && productDiscountsTouched!!) {
            map.add(Constants.DISCOUNT, "")
        }

        return map
    }

    companion object {
        val reservedProps: MutableList<String>
            get() {
                val reserved = BaseEntityImpl.reservedProps
                reserved.add(Constants.NAME)
                reserved.add(Constants.VERSION)
                reserved.add(Constants.Product.LICENSEE_AUTO_CREATE)
                reserved.add(Constants.Product.DESCRIPTION)
                reserved.add(Constants.Product.LICENSING_INFO)
                reserved.add(Constants.DISCOUNT)
                reserved.add(Constants.IN_USE)
                return reserved
            }
    }
}
