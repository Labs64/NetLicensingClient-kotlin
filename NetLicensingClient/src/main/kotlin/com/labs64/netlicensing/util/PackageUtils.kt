package com.labs64.netlicensing.util

object PackageUtils {
    fun getImplementationVersion(): String {
        // get implementation version
        return PackageUtils::class.java.getPackage().implementationVersion
    }
}