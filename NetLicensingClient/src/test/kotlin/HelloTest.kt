package tests

import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.SecurityMode
import com.labs64.netlicensing.service.UtilityService
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test

class HelloTest {

    /**
     * Exit codes
     */
    private val CODE_OK = 0
    private val CODE_ERROR = 1

    private val DEMO_NUMBER_PREFIX = "DEMO-"

    @Test fun testWhy() {
        val context = Context()
        context.setBaseUrl("http://localhost:28080/core/v2/rest")
        context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION)
        context.setUsername("vendor")
        context.setPassword("vendor")

        val randomNumber = RandomStringUtils.randomAlphanumeric(8)
        val productNumber = numberWithPrefix("P", randomNumber)
        val productModuleNumber = numberWithPrefix("PM", randomNumber)
        val licenseTemplateNumber = numberWithPrefix("LT", randomNumber)
        val licenseeNumber = numberWithPrefix("L", randomNumber)
        val licenseNumber = numberWithPrefix("LC", randomNumber)
        val licenseeName = numberWithPrefix("Licensee ", RandomStringUtils.randomAlphanumeric(8))

        println("asdf")
        println(context)
        val licenseTypes = UtilityService.listCountries(context, null)
    }

    private fun numberWithPrefix(prefix: String, number: String): String {
        return String.format("%s%s%s", DEMO_NUMBER_PREFIX, prefix, number)
    }
}
