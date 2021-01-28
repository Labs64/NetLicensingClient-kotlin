package com.labs64.netlicensing.examples

import org.junit.Test
import java.lang.Exception
import kotlin.test.fail

class RunExamples {
    @Test
    fun testCallEveryAPIMethod() {
        CallEveryAPIMethod().execute()
    }

    @Test
    fun testOfflineValidation() {
        try {
            OfflineValidation().execute()
        } catch (e: Exception) {
            fail(e.message)
        }
    }
}