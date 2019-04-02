package com.labs64.netlicensing.domain

/**
 * The class Constants declares entities field names and properties constants.
 */
object Constants {

    // CHECKSTYLE:OFF

    val ID = "id"
    val ACTIVE = "active"
    val NUMBER = "number"
    val NAME = "name"
    val VERSION = "version"
    val DELETED = "deleted"
    val CASCADE = "forceCascade"
    val PRICE = "price"
    val DISCOUNT = "discount"
    val CURRENCY = "currency"
    val IN_USE = "inUse"
    val FILTER = "filter"
    val BASE_URL = "baseUrl"
    val USERNAME = "username"
    val PASSWORD = "password"
    val SECURITY_MODE = "securityMode"
    val PROP_ID = "ID"

    object Utility {
        val ENDPOINT_PATH = "utility"
        val ENDPOINT_PATH_LICENSE_TYPES = "licenseTypes"
        val ENDPOINT_PATH_LICENSING_MODELS = "licensingModels"
        val LICENSING_MODEL_PROPERTIES = "LicensingModelProperties"
        val LICENSE_TYPE = "LicenseType"
    }

    object Token {
        val ENDPOINT_PATH = "token"
        val EXPIRATION_TIME = "expirationTime"
        val TOKEN_TYPE = "tokenType"

        val API_KEY = "apiKey"

        val TOKEN_PROP_EMAIL = "email"
        val TOKEN_PROP_VENDORNUMBER = "vendorNumber"
        val TOKEN_PROP_SHOP_URL = "shopURL"
    }

    object Vendor {
        val VENDOR_NUMBER = "vendorNumber"
        val VENDOR_TYPE = "Vendor"
    }

    object Product {
        val ENDPOINT_PATH = "product"
        val PRODUCT_NUMBER = "productNumber"
        val LICENSEE_AUTO_CREATE = "licenseeAutoCreate"
        val DESCRIPTION = "description"
        val LICENSING_INFO = "licensingInfo"
        val DISCOUNTS = "discounts"
        val PROP_LICENSEE_SECRET_MODE = "licenseeSecretMode"
        val PROP_VAT_MODE = "vatMode"

        object Discount {
            val TOTAL_PRICE = "totalPrice"
            val AMOUNT_FIX = "amountFix"
            val AMOUNT_PERCENT = "amountPercent"
        }
    }

    object ProductModule {
        val ENDPOINT_PATH = "productmodule"
        val PRODUCT_MODULE_NUMBER = "productModuleNumber"
        val PRODUCT_MODULE_NAME = "productModuleName"
        val LICENSING_MODEL = "licensingModel"
    }

    object LicenseTemplate {
        val ENDPOINT_PATH = "licensetemplate"
        val LICENSE_TEMPLATE_NUMBER = "licenseTemplateNumber"
        val LICENSE_TYPE = "licenseType"
        val AUTOMATIC = "automatic"
        val HIDDEN = "hidden"
        val HIDE_LICENSES = "hideLicenses"
    }

    object Licensee {
        val ENDPOINT_PATH = "licensee"
        val ENDPOINT_PATH_VALIDATE = "validate"
        val ENDPOINT_PATH_TRANSFER = "transfer"
        val LICENSEE_NUMBER = "licenseeNumber"
        val SOURCE_LICENSEE_NUMBER = "sourceLicenseeNumber"
        val PROP_LICENSEE_NAME = "licenseeName"
        val PROP_LICENSEE_SECRET = "licenseeSecret"
        val PROP_MARKED_FOR_TRANSFER = "markedForTransfer"
    }

    object License {
        val ENDPOINT_PATH = "license"
        val HIDDEN = "hidden"
        val LICENSE_NUMBER = "licenseNumber"
    }

    object LicensingModel {

        val VALID = "valid"

        object TryAndBuy {
            val NAME = "TryAndBuy"
        }

        object Rental {
            val NAME = "Rental"

            @Deprecated("please use com.labs64.netlicensing.domain.Constants.LicensingModel#VALID instead.")
            val VALID = "valid"
            val RED_THRESHOLD = "redThreshold"
            val YELLOW_THRESHOLD = "yellowThreshold"
            val EXPIRATION_WARNING_LEVEL = "expirationWarningLevel"
        }

        object Subscription {
            val NAME = "Subscription"
        }

        object Floating {
            val NAME = "Floating"
        }

        object MultiFeature {
            val NAME = "MultiFeature"
        }

        object PayPerUse {
            val NAME = "PayPerUse"
        }

        object PricingTable {
            val NAME = "PricingTable"
            val PROP_PLAN = "plan"
            val PROP_SKU = "sku"
        }

        object Quota {
            val NAME = "Quota"
        }
    }

    object Transaction {
        val ENDPOINT_PATH = "transaction"
        val TRANSACTION_NUMBER = "transactionNumber"
        val GRAND_TOTAL = "grandTotal"
        val STATUS = "status"
        val SOURCE = "source"
        val DATE_CREATED = "datecreated"
        val DATE_CLOSED = "dateclosed"
        val VAT = "vat"
        val VAT_MODE = "vatMode"
        val LICENSE_TRANSACTION_JOIN = "licenseTransactionJoin"
        val SOURCE_SHOP_ONLY = "shopOnly"
    }

    object PaymentMethod {
        val ENDPOINT_PATH = "paymentmethod"
    }

    object Country {
        val ENDPOINT_PATH = "countries"
        val CODE = "code"
        val NAME = "name"
        val VAT_PERCENT = "vatPercent"
        val IS_EU = "isEu"
    }

    object Shop {
        val PROP_SHOP_LICENSE_ID = "shop-license-id"
        val PROP_SHOPPING_CART = "shopping-cart"
    }

    object ValidationResult {
        val VALIDATION_RESULT_TYPE = "ProductModuleValidation"
        val DEFAULT_TTL_MINUTES = 60 * 24 // 1 day
    }

    // CHECKSTYLE:ON
}