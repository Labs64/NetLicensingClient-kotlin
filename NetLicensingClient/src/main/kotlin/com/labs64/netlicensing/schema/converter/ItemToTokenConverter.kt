package com.labs64.netlicensing.schema.converter

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Token
import com.labs64.netlicensing.domain.entity.impl.TokenImpl
import com.labs64.netlicensing.domain.vo.TokenType
import com.labs64.netlicensing.exception.ConversionException
import com.labs64.netlicensing.schema.SchemaFunction
import com.labs64.netlicensing.schema.context.Item
import com.labs64.netlicensing.util.DateUtils

class ItemToTokenConverter : ItemToEntityBaseConverter<Token>() {

    @Throws(ConversionException::class)
    override fun convert(source: Item?): Token {
        val target = super.convert(source)

        source?.let {
            if (SchemaFunction.propertyByName(source.property, Constants.Token.EXPIRATION_TIME).value != null) {
                target.expirationTime = DateUtils.parseDate(
                    SchemaFunction.propertyByName(
                        source.property, Constants.Token.EXPIRATION_TIME
                    ).value
                ).time
            }

            target.tokenType = TokenType.parseString(
                SchemaFunction.propertyByName(
                    source.property,
                    Constants.Token.TOKEN_TYPE
                ).value
            )
            target.vendorNumber = SchemaFunction.propertyByName(
                source.property,
                Constants.Token.TOKEN_PROP_VENDORNUMBER
            ).value

            // Custom properties
            for (property in source.property) {
                if (!TokenImpl.reservedProps.contains(property.name)) {
                    target.addProperty(property.name, property.value)
                }
            }
        }

        return target
    }

    public override fun newTarget(): Token {
        return TokenImpl()
    }
}
