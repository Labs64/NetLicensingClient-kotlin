package com.labs64.netlicensing.domain.entity.impl

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Token
import com.labs64.netlicensing.domain.vo.TokenType
import java.util.Date
import javax.ws.rs.core.MultivaluedMap

/**
 * Default implementation of [com.labs64.netlicensing.domain.entity.Token].
 */
class TokenImpl : BaseEntityImpl(), Token {

    override var vendorNumber: String? = null

    override var expirationTime: Date? = null
        set(expirationTime) {
            if (expirationTime == null) {
                field = null
            } else {
                field = Date(expirationTime.time)
            }
        }
        get() {
            if (field == null) {
                return null
            }
            return Date(field!!.time)
        }

    override var tokenType: TokenType? = null

    val tokenProperties: Map<String, String>
        get() = properties

    override fun asPropertiesMap(): MultivaluedMap<String, Any> {
        val map = super.asPropertiesMap()
        map.add(Constants.Token.EXPIRATION_TIME, expirationTime)
        map.add(Constants.Token.TOKEN_TYPE, tokenType)
        map.add(Constants.Token.TOKEN_PROP_VENDORNUMBER, vendorNumber)
        return map
    }

    companion object {
        /**
         * @see BaseEntityImpl.getReservedProps
         */
        val reservedProps: List<String>
            get() {
                val reserved = BaseEntityImpl.reservedProps
                reserved.add(Constants.Token.EXPIRATION_TIME)
                reserved.add(Constants.Token.TOKEN_TYPE)
                return reserved
            }
    }
}
