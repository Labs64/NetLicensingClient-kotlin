package tests

import getHelloString
import kotlin.test.assertEquals
import org.junit.Test

class HelloTest {
    @Test fun testAssert() {
        assertEquals("Hello, world!", getHelloString())
    }
}
