package io.curity.identityserver.plugin.username.authentication

import se.curity.identityserver.sdk.haapi.HaapiContract
import se.curity.identityserver.sdk.haapi.Message
import se.curity.identityserver.sdk.haapi.RepresentationFactory
import se.curity.identityserver.sdk.haapi.RepresentationFunction
import se.curity.identityserver.sdk.haapi.RepresentationModel
import se.curity.identityserver.sdk.http.HttpMethod
import se.curity.identityserver.sdk.web.Representation
import java.net.URI

class GetRepresentationFunction : RepresentationFunction {

    override fun apply(representationModel: RepresentationModel, factory: RepresentationFactory): Representation {

        val authUrl = representationModel.getString("_authUrl").let { URI.create(it) }
        val username = representationModel.getString("username")

        return factory.newAuthenticationStep { step ->
            step.addFormAction(HaapiContract.Actions.Kinds.LOGIN,
                    authUrl,
                    HttpMethod.POST,
                    null,
                    Message.ofKey("view.authenticate"),
                    Message.ofKey("view.submit"))
            { fields ->
                fields.addUsernameField("username",
                        Message.ofKey("view.username"), username)
            }
        }
    }
}
