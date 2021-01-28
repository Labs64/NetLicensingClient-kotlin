package com.labs64.netlicensing.examples

import com.labs64.netlicensing.domain.vo.Context
import com.labs64.netlicensing.domain.vo.MetaInfo
import com.labs64.netlicensing.schema.context.Netlicensing
import com.labs64.netlicensing.service.ValidationService.validateOffline
import com.labs64.netlicensing.utils.ConsoleWriter
import com.labs64.netlicensing.utils.TestHelpers
import com.labs64.netlicensing.utils.TestHelpers.loadFileContent
import java.io.StringReader
import javax.xml.bind.JAXBContext

class OfflineValidation : NetLicensingExample {
    override fun execute() {
        val out = ConsoleWriter()

        // 1. Create context, for offline validation you only need to provide the public key.
        val publicKey: String = TestHelpers.loadFileContent("rsa_public.pem")
        val context = Context()
        context.setPublicKey(publicKey)

        // 2. Read the validation file as 'Netlicensing' object.
        val offlineValidation = loadFileContent("Isb-DEMO.xml")
        val jaxbContext = JAXBContext.newInstance(Netlicensing::class.java)
        val jaxbUnmarshaller = jaxbContext.createUnmarshaller()
        val reader = StringReader(offlineValidation)
        val validationFile = jaxbUnmarshaller.unmarshal(reader) as Netlicensing

        // 3. Validate. ValidationResult is same as if validation would be executed against the
        // NetLicensing service online.
        val meta = MetaInfo()
        val validationResult = validateOffline(context, validationFile, meta)

        out.writeObject("Validation result (Offline / signed):", validationResult)
    }
}