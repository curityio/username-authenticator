package io.curity.identityserver.plugin.username.descriptor

import io.curity.identityserver.plugin.username.authentication.UsernameAuthenticatorRequestHandler
import io.curity.identityserver.plugin.username.config.UsernameAuthenticatorPluginConfig
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler
import se.curity.identityserver.sdk.plugin.descriptor.AuthenticatorPluginDescriptor

class UsernameAuthenticatorPluginDescriptor : AuthenticatorPluginDescriptor<UsernameAuthenticatorPluginConfig>
{
    override fun getAuthenticationRequestHandlerTypes(): Map<String, Class<out AuthenticatorRequestHandler<*>>> =
            mapOf("index" to UsernameAuthenticatorRequestHandler::class.java)
    
    override fun getConfigurationType(): Class<out UsernameAuthenticatorPluginConfig> =
        UsernameAuthenticatorPluginConfig::class.java
    
    override fun getPluginImplementationType(): String = "username"
}