package com.labs64.netlicensing.provider

import com.labs64.netlicensing.provider.auth.Authentication
import com.labs64.netlicensing.provider.auth.TokenAuthentication
import com.labs64.netlicensing.provider.auth.UsernamePasswordAuthentication

abstract class AbstractRestProvider : RestProvider {

    protected var authentication: Authentication? = null
        private set

    protected var configuration: RestProvider.Configuration? = null
        private set

    override fun authenticate(authentication: Authentication): RestProvider {
        this.authentication = authentication
        return this
    }

    override fun authenticate(username: String, password: String): RestProvider {
        authentication = UsernamePasswordAuthentication(username, password)
        return this
    }

    override fun authenticate(token: String): RestProvider {
        authentication = TokenAuthentication(token)
        return this
    }

    override fun configure(configuration: RestProvider.Configuration) {
        this.configuration = configuration
    }

}