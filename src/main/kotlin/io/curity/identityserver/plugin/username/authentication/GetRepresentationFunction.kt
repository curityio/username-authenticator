package io.curity.identityserver.plugin.username.authentication

import se.curity.identityserver.sdk.haapi.HaapiContract
import se.curity.identityserver.sdk.haapi.Message
import se.curity.identityserver.sdk.haapi.RepresentationFactory
import se.curity.identityserver.sdk.haapi.RepresentationFunction
import se.curity.identityserver.sdk.haapi.RepresentationModel
import se.curity.identityserver.sdk.http.HttpMethod
import se.curity.identityserver.sdk.http.MediaType
import se.curity.identityserver.sdk.web.Representation
import java.net.URI

class GetRepresentationFunction : RepresentationFunction
{
    companion object
    {
        val titleMessage: Message = Message.ofKey("view.authenticate")
        val submitMessage: Message = Message.ofKey("view.submit")
        val usernameMessage: Message = Message.ofKey("view.username")
    }

    override fun apply(representationModel: RepresentationModel, factory: RepresentationFactory): Representation
    {
        val authUrl = representationModel.getString("_authUrl").let { URI.create(it) }
        val username = representationModel.getOptionalString("username").orElse(null)

        return factory.newAuthenticationStep { step ->
            step.addFormAction(HaapiContract.Actions.Kinds.LOGIN,
                    authUrl,
                    HttpMethod.POST,
                    MediaType.X_WWW_FORM_URLENCODED,
                    titleMessage,
                    submitMessage)
            { fields ->
                fields.addUsernameField("username", usernameMessage, username)
            }
        }
    }
}
