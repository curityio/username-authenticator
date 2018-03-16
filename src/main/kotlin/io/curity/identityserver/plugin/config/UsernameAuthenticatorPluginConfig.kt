package io.curity.identityserver.plugin.config;

import se.curity.identityserver.sdk.config.Configuration
import se.curity.identityserver.sdk.service.UserPreferenceManager

interface UsernameAuthenticatorPluginConfig : Configuration {
    val userPreferencesManager: UserPreferenceManager
}
