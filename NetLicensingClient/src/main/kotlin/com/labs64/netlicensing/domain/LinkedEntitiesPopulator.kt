package com.labs64.netlicensing.domain

import com.labs64.netlicensing.domain.entity.License
import com.labs64.netlicensing.domain.entity.LicenseTemplate
import com.labs64.netlicensing.domain.entity.Licensee
import com.labs64.netlicensing.domain.entity.Product
import com.labs64.netlicensing.domain.entity.ProductModule
import com.labs64.netlicensing.util.Visitable
import com.labs64.netlicensing.util.Visitor
import java.util.ArrayList

class LinkedEntitiesPopulator(private val linkedEntities: MutableList<Any>) : Visitor() {

    @Throws(Exception::class)
    fun visit(product: Product) {
        val linkedProductModules = ArrayList<ProductModule>()
        val iter = linkedEntities.iterator()
        while (iter.hasNext()) {
            val linkedEntity = iter.next()
            if (ProductModule::class.java.isAssignableFrom(linkedEntity.javaClass)) {
                val linkedProductModule = linkedEntity as ProductModule
                if (product.number == linkedProductModule.product!!.number) {
                    iter.remove()
                    linkedProductModules.add(linkedProductModule)
                    linkedProductModule.product = product
                }
            } else if (Licensee::class.java.isAssignableFrom(linkedEntity.javaClass)) {
                val linkedLecensee = linkedEntity as Licensee
                if (product.number == linkedLecensee.product!!.number) {
                    iter.remove()
                    linkedLecensee.product = product
                }
            }
        }
        for (linkedProductModule in linkedProductModules) {
            if (Visitable::class.java.isAssignableFrom(linkedProductModule.javaClass)) {
                (linkedProductModule as Visitable).accept(this)
            }
        }
    }

    @Throws(Exception::class)
    fun visit(productModule: ProductModule) {
        val linkedLicenseTemplates = ArrayList<LicenseTemplate>()
        val iter = linkedEntities.iterator()
        while (iter.hasNext()) {
            val linkedEntity = iter.next()
            if (LicenseTemplate::class.java.isAssignableFrom(linkedEntity.javaClass)) {
                val linkedLicenseTemplate = linkedEntity as LicenseTemplate
                if (productModule.number == linkedLicenseTemplate.productModule!!.number) {
                    iter.remove()
                    linkedLicenseTemplates.add(linkedLicenseTemplate)
                    linkedLicenseTemplate.productModule = productModule
                }
            }
        }
        for (linkedLicenseTemplate in linkedLicenseTemplates) {
            if (Visitable::class.java.isAssignableFrom(linkedLicenseTemplate.javaClass)) {
                (linkedLicenseTemplate as Visitable).accept(this)
            }
        }
    }

    @Throws(Exception::class)
    fun visit(licenseTemplate: LicenseTemplate) {
        val iter = linkedEntities.iterator()
        while (iter.hasNext()) {
            val linkedEntity = iter.next()
            if (License::class.java.isAssignableFrom(linkedEntity.javaClass)) {
                val linkedLicense = linkedEntity as License
                if (licenseTemplate.number == linkedLicense.licenseTemplate!!.number) {
                    iter.remove()
                    linkedLicense.licenseTemplate = licenseTemplate
                }
            }
        }
    }
}