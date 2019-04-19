package com.labs64.netlicensing.domain.vo

enum class TokenType {
    DEFAULT, APIKEY, REGISTRATION, PASSWORDRESET, SHOP;

    companion object {
        fun parseString(token: String?): TokenType? {
            if (token != null) {
                for (tokenType in TokenType.values()) {
                    if (token.equals(tokenType.name, ignoreCase = true)) {
                        return tokenType
                    }
                }
            }
            return null
        }
    }
}