package com.labs64.netlicensing.service

import com.labs64.netlicensing.domain.Constants
import com.labs64.netlicensing.domain.entity.Token
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.Page
import com.labs64.netlicensing.exception.NetLicensingException
import com.labs64.netlicensing.util.CheckUtils
import org.apache.commons.lang3.StringUtils
import java.util.HashMap

object TokenService {

    /**
     * Gets token by its number.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * the token number
     * @return the token
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    operator fun get(context: Context, number: String): Token? {
        CheckUtils.paramNotEmpty(number, "number")
        val params = HashMap<String, Any?>()
        return NetLicensingService.getInstance()
            ?.get(context, Constants.Token.ENDPOINT_PATH + "/" + number, params, Token::class.java)
    }

    /**
     * Returns tokens of a vendor.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param filter
     * additional criteria to filter type of tokens to return, if NULL return tokens of all types
     * @return collection of token entities or null/empty list if nothing found.
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun list(context: Context, filter: String): Page<Token>? {
        val params = HashMap<String, Any?>()
        if (StringUtils.isNotBlank(filter)) {
            params[Constants.FILTER] = filter
        }
        return NetLicensingService.getInstance()?.list(context, Constants.Token.ENDPOINT_PATH, params, Token::class.java)
    }

    /**
     * Creates new token.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param token
     * non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return created token
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun create(context: Context, token: Token): Token? {
        CheckUtils.paramNotNull(token, "token")

        return NetLicensingService.getInstance()
            ?.post(context, Constants.Token.ENDPOINT_PATH, token.asRequestForm(), Token::class.java)
    }

    /**
     * Delete token by its number.
     *
     * @param context
     * determines the vendor on whose behalf the call is performed
     * @param number
     * the token number
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     * any subclass of [com.labs64.netlicensing.exception.NetLicensingException]. These
     * exceptions will be transformed to the corresponding service response messages.
     */
    @Throws(NetLicensingException::class)
    fun delete(context: Context, number: String) {
        CheckUtils.paramNotEmpty(number, "number")

        NetLicensingService.getInstance()?.delete(context, Constants.Token.ENDPOINT_PATH + "/" + number, null)
    }
}