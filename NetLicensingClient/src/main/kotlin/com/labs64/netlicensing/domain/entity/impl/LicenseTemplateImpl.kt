package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.License
import com.labs64.netlicensing.domain.entity.LicenseTemplate
import com.labs64.netlicensing.domain.entity.ProductModule
import com.labs64.netlicensing.domain.vo.Currency
import com.labs64.netlicensing.domain.vo.LicenseType
import java.math.BigDecimal
import java.util.ArrayList
import javax.ws.rs.core.MultivaluedMap

class LicenseTemplateImpl : BaseEntityImpl(), LicenseTemplate {

    override var productModule: ProductModule? = null
        set(productModule) {
            productModule?.licenseTemplates?.add(this)
            field = productModule
        }

    override var name: String? = null

    override var licenseType: LicenseType? = null

    override var price: BigDecimal? = null

    override var currency: Currency? = null

    override var automatic: Boolean? = java.lang.Boolean.FALSE

    override var hidden: Boolean? = java.lang.Boolean.FALSE

    override var hideLicenses: Boolean? = java.lang.Boolean.FALSE

    override var licenses: MutableCollection<License>? = null
        get() {
            if (field == null) {
                this.licenses = ArrayList()
            }
            return field
        }

    val licenseTemplateProperties: Map<String, String>
        get() = properties

    override fun asPropertiesMap(): MultivaluedMap<String, Any> {
        val map = super.asPropertiesMap()
        map.add(Constants.NAME, name)
        map.add(Constants.LicenseTemplate.LICENSE_TYPE, licenseType)
        map.add(Constants.PRICE, price)
        map.add(Constants.CURRENCY, currency)
        map.add(Constants.LicenseTemplate.AUTOMATIC, automatic)
        map.add(Constants.LicenseTemplate.HIDDEN, hidden)
        map.add(Constants.LicenseTemplate.HIDE_LICENSES, hideLicenses)
        return map
    }

    companion object {
        /**
         * @see BaseEntityImpl.getReservedProps
         */
        // maps to 'productModule'
        // maps to 'productModule'
        // used by shop in licenses, therefore disallowed for user
        // used by shop in licenses, therefore disallowed for user
        val reservedProps: List<String>
            get() {
                val reserved = BaseEntityImpl.reservedProps
                reserved.add(Constants.ProductModule.PRODUCT_MODULE_NUMBER)
                reserved.add(Constants.ProductModule.PRODUCT_MODULE_NAME)
                reserved.add(Constants.NAME)
                reserved.add(Constants.LicenseTemplate.LICENSE_TYPE)
                reserved.add(Constants.PRICE)
                reserved.add(Constants.CURRENCY)
                reserved.add(Constants.IN_USE)
                reserved.add(Constants.LicenseTemplate.AUTOMATIC)
                reserved.add(Constants.LicenseTemplate.HIDDEN)
                reserved.add(Constants.LicenseTemplate.HIDE_LICENSES)
                reserved.add(Constants.Shop.PROP_SHOP_LICENSE_ID)
                reserved.add(Constants.Shop.PROP_SHOPPING_CART)
                return reserved
            }
    }
}
