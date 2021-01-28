package com.labs64.netlicensing.util

import com.helger.commons.ValueEnforcer
import com.helger.commons.collection.ext.CommonsArrayList
import com.helger.commons.collection.ext.ICommonsList
import com.helger.xmldsig.XMLDSigSetup
import com.helger.xmldsig.XMLDSigValidationResult
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.security.Key
import javax.xml.crypto.MarshalException
import javax.xml.crypto.dsig.Reference
import javax.xml.crypto.dsig.XMLSignature
import javax.xml.crypto.dsig.XMLSignatureException
import javax.xml.crypto.dsig.dom.DOMValidateContext

object XMLDSigValidatorCustom {
    fun containsSignature(aDoc: Document): Boolean {
        ValueEnforcer.notNull(aDoc, "Document")
        val aSignatureNL = aDoc.getElementsByTagNameNS(XMLSignature.XMLNS, XMLDSigSetup.ELEMENT_SIGNATURE)
        return aSignatureNL.length > 0
    }

    @Throws(XMLSignatureException::class)
    fun validateSignature(aDoc: Document, aKey: Key): XMLDSigValidationResult {
        ValueEnforcer.notNull(aDoc, "Document")
        ValueEnforcer.notNull(aKey, "Key")

        // Find Signature element.
        val aSignatureNL = aDoc.getElementsByTagNameNS(XMLSignature.XMLNS, XMLDSigSetup.ELEMENT_SIGNATURE)
        require(aSignatureNL.length == 1) { "Cannot find exactly one Signature element." }
        val aSignatureElement = aSignatureNL.item(0) as Element

        // Create a DOM XMLSignatureFactory that will be used to validate the
        // enveloped signature.
        val aSignatureFactory = XMLDSigSetup.getXMLSignatureFactory()

        // Create a DOMValidateContext and specify a KeySelector
        // and document context.
        val aValidationContext = DOMValidateContext(aKey, aSignatureElement)

        // Unmarshal the XMLSignature.
        val aSignature: XMLSignature
        aSignature = try {
            aSignatureFactory.unmarshalXMLSignature(aValidationContext)
        } catch (ex: MarshalException) {
            return XMLDSigValidationResult.createSignatureError()
        }

        // Validate the XMLSignature.
        if (aSignature.validate(aValidationContext)) {
            return XMLDSigValidationResult.createSuccess()
        }

        // Core validation failed. Check the signature value.
        if (!aSignature.signatureValue.validate(aValidationContext)) {
            return XMLDSigValidationResult.createSignatureError()
        }

        // Check the validation status of each Reference.
        val aInvalidReferences: ICommonsList<Int> = CommonsArrayList()
        val it: Iterator<*> = aSignature.signedInfo.references.iterator()
        var nIndex = 0

        while (it.hasNext()) {
            val aReference = it.next() as Reference
            if (!aReference.validate(aValidationContext)) {
                aInvalidReferences.add(nIndex)
            }

            nIndex++
        }

        return XMLDSigValidationResult.createReferenceErrors(aInvalidReferences)
    }
}