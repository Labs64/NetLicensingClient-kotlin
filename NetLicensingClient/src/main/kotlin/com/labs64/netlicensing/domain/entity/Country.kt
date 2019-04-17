package com.labs64.netlicensing.domain.entity

import java.math.BigDecimal

/**
 * Country entity used internally by NetLicensing.
 *
 *
 * Properties visible via NetLicensing API:
 *
 *
 * **code** - Unique code of country.
 *
 *
 * **name** - Unique name of country
 *
 *
 * **vat** - Country vat.
 *
 *
 * **isEu** - is country in EU.
 *
 */
interface Country : BaseEntity {

    var code: String?

    var name: String?

    var vatPercent: BigDecimal?

    var isEu: Boolean?
}
