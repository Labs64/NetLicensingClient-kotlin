package com.labs64.netlicensing.utils

import org.apache.commons.io.IOUtils

import java.nio.charset.StandardCharsets

object TestHelpers {
    fun loadFileContent(fileName: String): String {
        val classloader = Thread.currentThread().contextClassLoader
        val inputStream = classloader.getResourceAsStream(fileName)
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    }
}