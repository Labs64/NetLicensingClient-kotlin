package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Country
import java.math.BigDecimal
import javax.ws.rs.core.MultivaluedMap

class CountryImpl : BaseEntityImpl(), Country {

    override var code: String? = null

    override var name: String? = null

    override var vatPercent: BigDecimal? = null

    override var isEu: Boolean? = false

    override fun asPropertiesMap(): MultivaluedMap<String, Any> {
        val map = super.asPropertiesMap()
        map.add(Constants.Country.CODE, code)
        map.add(Constants.Country.NAME, name)
        map.add(Constants.Country.VAT_PERCENT, vatPercent)
        map.add(Constants.Country.IS_EU, isEu)
        return map
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("Country [")
        builder.append(Constants.Country.CODE)
        builder.append("=")
        builder.append(code)
        builder.append(", ")
        builder.append(Constants.Country.NAME)
        builder.append("=")
        builder.append(name)
        builder.append(", ")
        builder.append(Constants.Country.VAT_PERCENT)
        builder.append("=")
        builder.append(vatPercent)
        builder.append(", ")
        builder.append(Constants.Country.IS_EU)
        builder.append("=")
        builder.append(isEu)
        builder.append("]")
        return builder.toString()
    }
}