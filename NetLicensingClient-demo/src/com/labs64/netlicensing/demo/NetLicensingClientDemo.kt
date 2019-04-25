package com.labs64.netlicensing.demo

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Licensee
import com.labs64.netlicensing.domain.entity.impl.LicenseImpl
import com.labs64.netlicensing.domain.entity.impl.LicenseTemplateImpl
import com.labs64.netlicensing.domain.entity.impl.LicenseeImpl
import com.labs64.netlicensing.domain.entity.impl.ProductImpl
import com.labs64.netlicensing.domain.entity.impl.ProductModuleImpl
import com.labs64.netlicensing.domain.entity.impl.TokenImpl
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Currency
import com.labs64.netlicensing.domain.vo.LicenseType
import com.labs64.netlicensing.domain.vo.LicenseeSecretMode
import com.labs64.netlicensing.domain.vo.SecurityMode
import com.labs64.netlicensing.domain.vo.TokenType
import com.labs64.netlicensing.domain.vo.ValidationParameters
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.service.LicenseService
import com.labs64.netlicensing.service.LicenseTemplateService
import com.labs64.netlicensing.service.LicenseeService
import com.labs64.netlicensing.service.PaymentMethodService
import com.labs64.netlicensing.service.ProductModuleService
import com.labs64.netlicensing.service.ProductService
import com.labs64.netlicensing.service.TokenService
import com.labs64.netlicensing.service.UtilityService
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import java.math.BigDecimal
import java.util.UUID

class NetLicensingClientDemo {

    companion object {

        private val CODE_OK = 0
        private val CODE_ERROR = 1

        private val DEMO_NUMBER_PREFIX = "DEMO-"

        @JvmStatic
        fun main(args: Array<String>) {

            val out = ConsoleWriter()

            val context = Context()
            context.setBaseUrl("https://go.netlicensing.io/core/v2/rest")
            context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION)
            context.setUsername("demo")
            context.setPassword("demo")

            val randomLicenseeSecret = UUID.randomUUID().toString()
            val randomNumber = RandomStringUtils.randomAlphanumeric(8)
            val productNumber = numberWithPrefix("P", randomNumber)
            val productModuleNumber = numberWithPrefix("PM", randomNumber)
            val licenseTemplateNumber = numberWithPrefix("LT", randomNumber)
            val licenseeNumber = numberWithPrefix("L", randomNumber)
            val licenseNumber = numberWithPrefix("LC", randomNumber)
            val licenseeName = numberWithPrefix("Licensee ", RandomStringUtils.randomAlphanumeric(8))

            var exitCode = CODE_OK
            try {
                // region ********* Lists

                val licenseTypes = UtilityService.listLicenseTypes(context)
                out.writePage("License Types:", licenseTypes)

                val licensingModels = UtilityService.listLicensingModels(context)
                out.writePage("Licensing Models:", licensingModels)

                val countries = UtilityService.listCountries(context, null)
                out.writePage("Countries:", countries)

                // endregion

                // region ********* Product

                val newProduct = ProductImpl()
                newProduct.number = productNumber
                newProduct.name = "Demo product"

                var product = ProductService.create(context, newProduct)
                out.writeObject("Added product:", product)

                product = ProductService.get(context, productNumber)
                out.writeObject("Got product:", product)

                var products = ProductService.list(context, null)
                out.writePage("Got the following products:", products)

                val updateProduct = ProductImpl()
                updateProduct.addProperty("Updated property name", "Updated value")
                product = ProductService.update(context, productNumber, updateProduct)
                out.writeObject("Updated product:", product)

                ProductService.delete(context, productNumber, true)
                out.writeMessage("Deleted Product!")

                products = ProductService.list(context, null)
                out.writePage("Got the following Products:", products)

                product = ProductService.create(context, newProduct)
                out.writeObject("Added product again:", product)

                products = ProductService.list(context, null)
                out.writePage("Got the following Products:", products)

                // endregion

                // region ********* ProductModule

                val newProductModule = ProductModuleImpl()
                newProductModule.number = productModuleNumber
                newProductModule.name = "Demo product module"
                newProductModule.licensingModel = Constants.LicensingModel.TryAndBuy.NAME
                var productModule = ProductModuleService.create(context, productNumber, newProductModule)
                out.writeObject("Added product module:", productModule)

                productModule = ProductModuleService[context, productModuleNumber]
                out.writeObject("Got product module:", productModule)

                var productModules = ProductModuleService.list(context, null)
                out.writePage("Got the following product modules:", productModules)

                val updateProductModule = ProductModuleImpl()
                updateProductModule.addProperty("Updated property name", "Updated property value")
                productModule = ProductModuleService.update(context, productModuleNumber, updateProductModule)
                out.writeObject("Updated product module:", productModule)

                ProductModuleService.delete(context, productModuleNumber, true)
                out.writeMessage("Deleted product module!")

                productModules = ProductModuleService.list(context, null)
                out.writePage("Got the following product modules:", productModules)

                productModule = ProductModuleService.create(context, productNumber, newProductModule)
                out.writeObject("Added product module again:", productModule)

                productModules = ProductModuleService.list(context, null)
                out.writePage("Got the following product modules:", productModules)
                // endregion

                // region ********* LicenseTemplate

                val newLicenseTemplate = LicenseTemplateImpl()
                newLicenseTemplate.number = licenseTemplateNumber
                newLicenseTemplate.name = "Demo Evaluation Period"
                newLicenseTemplate.licenseType = LicenseType.FEATURE
                newLicenseTemplate.price = BigDecimal(12.5)
                newLicenseTemplate.currency = Currency.EUR
                newLicenseTemplate.automatic = false
                newLicenseTemplate.hidden = false
                out.writeObject("Adding license template:", newLicenseTemplate)
                var licenseTemplate = LicenseTemplateService.create(
                    context, productModuleNumber,
                    newLicenseTemplate
                )
                out.writeObject("Added license template:", licenseTemplate)

                licenseTemplate = LicenseTemplateService[context, licenseTemplateNumber]
                out.writeObject("Got licenseTemplate:", licenseTemplate)

                var licenseTemplates = LicenseTemplateService.list(context, null)
                out.writePage("Got the following license templates:", licenseTemplates)

                val updateLicenseTemplate = LicenseTemplateImpl()
                updateLicenseTemplate.addProperty("Updated property name", "Updated value")
                licenseTemplate = LicenseTemplateService.update(
                    context, licenseTemplateNumber,
                    updateLicenseTemplate
                )
                out.writeObject("Updated license template:", licenseTemplate)

                LicenseTemplateService.delete(context, licenseTemplateNumber, true)
                out.writeMessage("Deleted license template!")

                licenseTemplates = LicenseTemplateService.list(context, null)
                out.writePage("Got the following license templates:", licenseTemplates)

                licenseTemplate = LicenseTemplateService.create(context, productModuleNumber, newLicenseTemplate)
                out.writeObject("Added license template again:", licenseTemplate)

                licenseTemplates = LicenseTemplateService.list(context, null)
                out.writePage("Got the following license templates:", licenseTemplates)

                // endregion

                // region ********* Licensee

                val newLicensee = LicenseeImpl()
                newLicensee.number = licenseeNumber
                var licensee = LicenseeService.create(context, productNumber, newLicensee)
                out.writeObject("Added licensee:", licensee)

                var licensees = LicenseeService.list(context, null)
                out.writePage("Got the following licensees:", licensees)

                LicenseeService.delete(context, licenseeNumber, true)
                out.writeMessage("Deleted licensee!")

                licensees = LicenseeService.list(context, null)
                out.writePage("Got the following licensees after delete:", licensees)

                licensee = LicenseeService.create(context, productNumber, newLicensee)
                out.writeObject("Added licensee again:", licensee)

                licensee = LicenseeService[context, licenseeNumber]
                out.writeObject("Got licensee:", licensee)

                val updateLicensee = LicenseeImpl()
                updateLicensee.addProperty("Updated property name", "Updated value")

                licensee = LicenseeService.update(context, licenseeNumber, updateLicensee)
                out.writeObject("Updated licensee:", licensee)

                licensees = LicenseeService.list(context, null)
                out.writePage("Got the following licensees:", licensees)

                // endregion

                // region ********* License

                val newLicense = LicenseImpl()
                newLicense.number = licenseNumber
                var license = LicenseService.create(
                    context, licenseeNumber, licenseTemplateNumber, null,
                    newLicense
                )
                out.writeObject("Added license:", license)

                var licenses = LicenseService.list(context, null)
                out.writePage("Got the following licenses:", licenses)

                LicenseService.delete(context, licenseNumber, true)
                out.writeMessage("Deleted license!")

                licenses = LicenseService.list(context, null)
                out.writePage("Got the following licenses:", licenses)

                license = LicenseService.create(
                    context, licenseeNumber, licenseTemplateNumber, null,
                    newLicense
                )
                out.writeObject("Added license again:", license)

                license = LicenseService[context, licenseNumber]
                out.writeObject("Got license:", license)

                val updateLicense = LicenseImpl()
                updateLicense.addProperty("Updated property name", "Updated value")
                license = LicenseService.update(context, licenseNumber, null, updateLicense)
                out.writeObject("Updated license:", license)

                // endregion

                // region ********* PaymentMethod

                val paymentMethods = PaymentMethodService.list(context, null)
                out.writePage("Got the following payment methods:", paymentMethods)

                // endregion

                // region ********* Token

                val newToken = TokenImpl()
                newToken.tokenType = TokenType.APIKEY
                val apiKey = TokenService.create(context, newToken)
                out.writeObject("Created APIKey:", apiKey)

                context.setApiKey(apiKey!!.number!!)
                newToken.tokenType = TokenType.SHOP
                newToken.addProperty(Constants.Licensee.LICENSEE_NUMBER, licenseeNumber)
                context.setSecurityMode(SecurityMode.APIKEY_IDENTIFICATION)
                val shopToken = TokenService.create(context, newToken)
                context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION)
                out.writeObject("Got the following shop token:", shopToken)

                val filter = Constants.Token.TOKEN_TYPE + "=" + TokenType.SHOP.name
                var tokens = TokenService.list(context, filter)
                out.writePage("Got the following shop tokens:", tokens)

                TokenService.delete(context, shopToken!!.number!!)
                out.writeMessage("Deleted shop token!")

                tokens = TokenService.list(context, filter)
                out.writePage("Got the following shop tokens after delete:", tokens)

                // endregion

                // region ********* Validate

                val validationParameters = ValidationParameters()
                validationParameters.put(productModuleNumber, "paramKey", "paramValue")
                validationParameters.licenseeSecret = randomLicenseeSecret
                validationParameters.licenseeName = licenseeName
                validationParameters.productNumber = productNumber
                var validationResult = LicenseeService.validate(context, licenseeNumber, validationParameters)
                out.writeObject("Validation result for created licensee:", validationResult)

                context.setSecurityMode(SecurityMode.APIKEY_IDENTIFICATION)
                validationResult = LicenseeService.validate(context, licenseeNumber, validationParameters)
                context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION)
                out.writeObject("Validation repeated with APIKey:", validationResult)

                // endregion

                // region ********* Transfer
                var transferLicensee: Licensee = LicenseeImpl()
                transferLicensee.number = "TR$licenseeNumber"
                transferLicensee.properties?.put(Constants.Licensee.PROP_MARKED_FOR_TRANSFER, "true")
                transferLicensee = LicenseeService.create(context, productNumber, transferLicensee)!!
                out.writeObject("Added transfer licensee:", transferLicensee)

                val transferLicense = LicenseImpl()
                transferLicense.number = "LTR$licenseNumber"
                val newTransferLicense = LicenseService.create(
                    context, transferLicensee.number!!,
                    licenseTemplateNumber, null, transferLicense
                )
                out.writeObject("Added license for transfer:", newTransferLicense)

                LicenseeService.transfer(context, licensee?.number!!, transferLicensee.number!!)

                licenses = LicenseService.list(context, "licenseeNumber=" + licensee.number!!)
                out.writePage("Got the following licenses after transfer:", licenses)

                var transferLicenseeWithApiKey: Licensee? = LicenseeImpl()
                transferLicenseeWithApiKey!!.number = "Key$licenseeNumber"
                transferLicenseeWithApiKey.properties!!.put(
                    Constants.Licensee.PROP_MARKED_FOR_TRANSFER,
                    java.lang.Boolean.toString(true)
                )
                transferLicenseeWithApiKey = LicenseeService.create(context, productNumber, transferLicenseeWithApiKey)

                val transferLicenseWithApiKey = LicenseImpl()
                transferLicenseWithApiKey.number = "Key$licenseNumber"
                LicenseService.create(
                    context, transferLicenseeWithApiKey!!.number!!, licenseTemplateNumber, null,
                    transferLicenseWithApiKey
                )

                context.setSecurityMode(SecurityMode.APIKEY_IDENTIFICATION)
                LicenseeService.transfer(context, licensee.number!!, transferLicenseeWithApiKey.number!!)
                context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION)

                licenses = LicenseService.list(context, "licenseeNumber=" + licensee.number!!)
                out.writePage("Got the following licenses after transfer:", licenses)

                // endregion

                // region ********* Transactions

                var transactions = com.labs64.netlicensing.service.TransactionService.list(
                    context,
                    Constants.Transaction.SOURCE_SHOP_ONLY + "=" + java.lang.Boolean.TRUE.toString()
                )
                out.writePage("Got the following transactions shop only:", transactions)

                transactions = com.labs64.netlicensing.service.TransactionService.list(context, null)
                out.writePage("Got the following transactions after transfer:", transactions)
                // endregion

                out.writeMessage("All done.")
            } catch (e: NetLicensingException) {
                out.writeException("Got NetLicensing exception:", e);
                exitCode = CODE_ERROR;
            } catch (e: Exception) {
                out.writeException("Got exception:", e);
                exitCode = CODE_ERROR;
            } finally {
                // Cleanup
                try {
                    // delete APIKey in case it was used (exists)
                    if (StringUtils.isNotBlank(context.getApiKey())) {
                        context.getApiKey()?.let {
                            TokenService.delete(context, it);
                            context.setApiKey(null);
                        }
                    }

                    // delete test product with all its related items
                    ProductService.delete(context, productNumber, true);

                } catch (e: NetLicensingException) {
                    out.writeException("Got NetLicensing exception during cleanup:", e);
                    exitCode = CODE_ERROR;
                } catch (e: Exception) {
                    out.writeException("Got exception during cleanup:", e);
                    exitCode = CODE_ERROR;
                }
            }

            if (exitCode == CODE_ERROR) {
                System.exit(exitCode)
            }
        }

        private fun numberWithPrefix(prefix: String, number: String): String {
            return String.format("%s%s%s", DEMO_NUMBER_PREFIX, prefix, number)
        }
    }
}