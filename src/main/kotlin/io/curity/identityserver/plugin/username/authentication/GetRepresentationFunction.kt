package io.curity.identityserver.plugin.username.authentication

import io.curity.identityserver.plugin.username.authentication.UsernameAuthenticatorRequestHandler.Companion.ADD_CONTEXT
import se.curity.identityserver.sdk.haapi.HaapiContract
import se.curity.identityserver.sdk.haapi.HaapiContract.Links.Relations.REGISTER_CREATE
import se.curity.identityserver.sdk.haapi.Message
import se.curity.identityserver.sdk.haapi.RepresentationFactory
import se.curity.identityserver.sdk.haapi.RepresentationFunction
import se.curity.identityserver.sdk.haapi.RepresentationModel
import se.curity.identityserver.sdk.http.HttpMethod
import se.curity.identityserver.sdk.http.MediaType
import se.curity.identityserver.sdk.web.LinkRelation
import se.curity.identityserver.sdk.web.Representation
import java.net.URI
import kotlin.collections.set

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
        val authUrl = representationModel.getString("_authUrl")
        val username = representationModel.getOptionalString("username").orElse(null)
        val showSetContextAttributeLink = representationModel.getOptionalBoolean("_showLinkToSetContextAttribute").orElse(false)

        return factory.newAuthenticationStep { step ->
            step.addFormAction(HaapiContract.Actions.Kinds.LOGIN,
                    URI.create(authUrl),
                    HttpMethod.POST,
                    MediaType.X_WWW_FORM_URLENCODED,
                    titleMessage,
                    submitMessage)
            { fields ->
                fields.addUsernameField("username", usernameMessage, username)
            }

            if (showSetContextAttributeLink) {
                val contextAttributeMessageKey = representationModel.getString("_contextAttributeMessageKey")
                step.addLink(URI.create(
                    "$authUrl?$ADD_CONTEXT"),
                    REGISTER_CREATE,
                    Message.ofKey("$contextAttributeMessageKey"))
            }
        }
    }
}
