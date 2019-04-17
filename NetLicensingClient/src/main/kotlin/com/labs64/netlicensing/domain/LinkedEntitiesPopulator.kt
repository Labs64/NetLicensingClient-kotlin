package com.labs64.netlicensing.domain

import com.labs64.netlicensing.util.Visitor
// TODO(AY): add items
class LinkedEntitiesPopulator(private val linkedEntities: MutableList<Any>) : Visitor() {

//    @Throws(Exception::class)
//    fun visit(product: Product) {
//        val linkedProductModules = ArrayList<E>()
//        val iter = linkedEntities.iterator()
//        while (iter.hasNext()) {
//            val linkedEntity = iter.next()
//            if (ProductModule::class.java!!.isAssignableFrom(linkedEntity.javaClass)) {
//                val linkedProductModule = linkedEntity as ProductModule
//                if (product.getNumber().equals(linkedProductModule.getProduct().getNumber())) {
//                    iter.remove()
//                    linkedProductModules.add(linkedProductModule)
//                    linkedProductModule.setProduct(product)
//                }
//            } else if (Licensee::class.java!!.isAssignableFrom(linkedEntity.javaClass)) {
//                val linkedLecensee = linkedEntity as Licensee
//                if (product.getNumber().equals(linkedLecensee.getProduct().getNumber())) {
//                    iter.remove()
//                    linkedLecensee.setProduct(product)
//                }
//            }
//        }
//        for (linkedProductModule in linkedProductModules) {
//            if (Visitable::class.java.isAssignableFrom(linkedProductModule.getClass())) {
//                (linkedProductModule as Visitable).accept(this)
//            }
//        }
//    }
//
//    @Throws(Exception::class)
//    fun visit(productModule: ProductModule) {
//        val linkedLicenseTemplates = ArrayList<E>()
//        val iter = linkedEntities.iterator()
//        while (iter.hasNext()) {
//            val linkedEntity = iter.next()
//            if (LicenseTemplate::class.java!!.isAssignableFrom(linkedEntity.javaClass)) {
//                val linkedLicenseTemplate = linkedEntity as LicenseTemplate
//                if (productModule.getNumber().equals(linkedLicenseTemplate.getProductModule().getNumber())) {
//                    iter.remove()
//                    linkedLicenseTemplates.add(linkedLicenseTemplate)
//                    linkedLicenseTemplate.setProductModule(productModule)
//                }
//            }
//        }
//        for (linkedLicenseTemplate in linkedLicenseTemplates) {
//            if (Visitable::class.java.isAssignableFrom(linkedLicenseTemplate.getClass())) {
//                (linkedLicenseTemplate as Visitable).accept(this)
//            }
//        }
//    }
//
//    @Throws(Exception::class)
//    fun visit(licenseTemplate: LicenseTemplate) {
//        val iter = linkedEntities.iterator()
//        while (iter.hasNext()) {
//            val linkedEntity = iter.next()
//            if (License::class.java!!.isAssignableFrom(linkedEntity.javaClass)) {
//                val linkedLicense = linkedEntity as License
//                if (licenseTemplate.getNumber().equals(linkedLicense.getLicenseTemplate().getNumber())) {
//                    iter.remove()
//                    linkedLicense.setLicenseTemplate(licenseTemplate)
//                }
//            }
//        }
//    }
}