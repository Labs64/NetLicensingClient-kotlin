package com.labs64.netlicensing.util

import com.helger.xmldsig.XMLDSigValidationResult
import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.exception.BadSignatureException
import com.labs64.netlicensing.schema.context.Netlicensing
import org.apache.commons.lang3.StringUtils
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.crypto.dsig.XMLSignatureException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

object SignatureUtils {
    @Throws(BadSignatureException::class)
    fun check(context: Context, response: Netlicensing) {
        if (StringUtils.isEmpty(context.getPublicKey())) {
            return
        }

        try {
            check(response, context.getPublicKey()!!.toByteArray())
        } catch (e: Exception) {
            throw BadSignatureException(e.message!!)
        }
    }

    @Throws(JAXBException::class, ParserConfigurationException::class, SignatureException::class)
    fun check(response: Netlicensing, publicKeyByteArray: ByteArray) {
        val jc = JAXBContext.newInstance(Netlicensing::class.java)

        // Create the Document
        val dbf = DocumentBuilderFactory.newInstance()
        dbf.isNamespaceAware = true
        val db = dbf.newDocumentBuilder()
        val doc = db.newDocument()
        doc.xmlStandalone = true

        // Marshal the NetLicensing to a Document
        val marshaller = jc.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        marshaller.marshal(response, doc)

        var isValidAssetsFile = false

        isValidAssetsFile = try {
            val publicKeyByte = Base64.getDecoder().decode(publicKeyByteArray)
            val spec = X509EncodedKeySpec(publicKeyByte)
            val kf = KeyFactory.getInstance("RSA")
            val publicKey = kf.generatePublic(spec)
            val validation: XMLDSigValidationResult = XMLDSigValidatorCustom.validateSignature(doc, publicKey)
            validation.isValid
        } catch (e: IllegalArgumentException) {
            throw SignatureException("Bad or empty response signature", e)
        } catch (e: XMLSignatureException) {
            throw SignatureException("Bad or empty response signature", e)
        } catch (e: NoSuchAlgorithmException) {
            throw SignatureException("Bad response signature", e)
        } catch (e: InvalidKeySpecException) {
            throw SignatureException("Bad response signature", e)
        }

        if (!isValidAssetsFile) {
            throw SignatureException("Response signature verification failure")
        }
    }
}