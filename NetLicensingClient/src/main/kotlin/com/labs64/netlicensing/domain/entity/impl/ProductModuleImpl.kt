package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.LicenseTemplate
import com.labs64.netlicensing.domain.entity.Product
import com.labs64.netlicensing.domain.entity.ProductModule
import java.util.ArrayList
import javax.ws.rs.core.MultivaluedMap

/**
 * Default implementation of [com.labs64.netlicensing.domain.entity.ProductModule].
 */
class ProductModuleImpl : BaseEntityImpl(), ProductModule {

    override var name: String? = null

    override var licensingModel: String? = null

    override var licenseTemplates: MutableCollection<LicenseTemplate>? = null
        get() {
            if (field == null) {
                this.licenseTemplates = ArrayList()
            }
            return field
        }

    val productModuleProperties: Map<String, String>
        get() = properties

    override var product: Product? = null
        set(product) {
            product?.productModules?.add(this)
            field = product
        }

    override fun asPropertiesMap(): MultivaluedMap<String, Any> {
        val map = super.asPropertiesMap()
        map.add(Constants.NAME, name)
        map.add(Constants.ProductModule.LICENSING_MODEL, licensingModel)
        return map
    }

    companion object {
        /**
         * @see BaseEntityImpl.getReservedProps
         */
        val reservedProps: List<String>
            get() {
                val reserved = BaseEntityImpl.reservedProps
                reserved.add(Constants.NAME)
                reserved.add(Constants.ProductModule.LICENSING_MODEL)
                reserved.add(Constants.Product.PRODUCT_NUMBER)
                reserved.add(Constants.IN_USE)
                return reserved
            }
    }
}