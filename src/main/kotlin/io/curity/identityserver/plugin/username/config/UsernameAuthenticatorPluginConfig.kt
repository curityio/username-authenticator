package io.curity.identityserver.plugin.username.config

import se.curity.identityserver.sdk.config.Configuration
import se.curity.identityserver.sdk.config.annotation.DefaultBoolean
import se.curity.identityserver.sdk.config.annotation.Description
import se.curity.identityserver.sdk.service.ExceptionFactory
import se.curity.identityserver.sdk.service.UserPreferenceManager
import java.util.*

interface UsernameAuthenticatorPluginConfig : Configuration
{
    val userPreferencesManager: UserPreferenceManager
    val exceptionFactory : ExceptionFactory

    @Description("If there is a preferred username in the username cookie, pass it on without prompting the user. " +
            "This cookie value can reside from a previous succesful authentication, or the client sending a " +
            "`login_hint`.")
    @DefaultBoolean(false)
    fun autoSubmitPreferredUserName(): Boolean

    fun getShowLinkToSetContextAttribute(): Optional<ContextAttributes>

    interface ContextAttributes {
        @Description("Message key present on the authenticator's link")
        fun getMessageKey(): String
        @Description("Name of the attribute added to context attributes")
        fun getContextAttributeName(): String
    }
}
