package tests

import getHelloString
import kotlin.test.assertEquals
import org.junit.Test

class HelloTest {
    @Test fun testAssert() : Unit {
        assertEquals("Hello, world!", getHelloString())
    }
}
