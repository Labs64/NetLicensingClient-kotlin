package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.License
import com.labs64.netlicensing.domain.entity.LicenseTemplate
import com.labs64.netlicensing.domain.entity.LicenseTransactionJoin
import com.labs64.netlicensing.domain.entity.Licensee
import com.labs64.netlicensing.domain.vo.Currency
import java.math.BigDecimal
import java.util.ArrayList
import javax.ws.rs.core.MultivaluedMap

class LicenseImpl : BaseEntityImpl(), License {

    override var name: String? = null

    override var price: BigDecimal? = null

    override var currency: Currency? = null

    override var hidden: Boolean? = java.lang.Boolean.FALSE

    override var licensee: Licensee? = null
        set(licensee) {
            licensee?.licenses!!.add(this)
            field = licensee
        }

    override var licenseTemplate: LicenseTemplate? = null
        set(licenseTemplate) {
            licenseTemplate?.licenses!!.add(this)
            field = licenseTemplate
        }

    override var licenseTransactionJoins: List<LicenseTransactionJoin>? = null
        get() {
            if (field == null) {
                this.licenseTransactionJoins = ArrayList()
            }
            return field
        }

    val licenseProperties: MutableMap<String, String>
        get() = properties

    override fun asPropertiesMap(): MultivaluedMap<String, Any> {
        val map = super.asPropertiesMap()
        map.add(Constants.NAME, name)
        map.add(Constants.PRICE, price)
        map.add(Constants.CURRENCY, currency)
        map.add(Constants.License.HIDDEN, hidden)
        return map
    }

    companion object {
        /**
         * @see BaseEntityImpl.getReservedProps
         */
        // maps to 'licensee'
        // maps to 'licenseTemplate'
        // maps to 'licenseTransactionJoins'
        // used by shop, therefore disallowed for user
        // used by shop, therefore disallowed for user
        // used by shop, therefore disallowed for user
        val reservedProps: List<String>
            get() {
                val reserved = BaseEntityImpl.reservedProps
                reserved.add(Constants.NAME)
                reserved.add(Constants.PRICE)
                reserved.add(Constants.CURRENCY)
                reserved.add(Constants.License.HIDDEN)
                reserved.add(Constants.Licensee.LICENSEE_NUMBER)
                reserved.add(Constants.LicenseTemplate.LICENSE_TEMPLATE_NUMBER)
                reserved.add(Constants.Transaction.TRANSACTION_NUMBER)
                reserved.add(Constants.Shop.PROP_SHOP_LICENSE_ID)
                reserved.add(Constants.Shop.PROP_SHOPPING_CART)
                reserved.add(Constants.Vendor.VENDOR_NUMBER)
                return reserved
            }
    }
}