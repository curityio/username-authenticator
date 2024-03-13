package io.curity.identityserver.plugin.username.authentication

import org.hibernate.validator.constraints.NotBlank
import se.curity.identityserver.sdk.service.UserPreferenceManager
import se.curity.identityserver.sdk.web.Request
import javax.validation.Valid

class RequestModel(request: Request, userPreferenceManager: UserPreferenceManager, contextAttributeName: String)
{
    @Valid
    val postRequestModel: Post? = if (request.isPostRequest) Post(request) else null

    @Valid
    val getRequestModel: Get? = if (request.isGetRequest) Get(request, userPreferenceManager, contextAttributeName) else null
}

class Post(request: Request)
{
    @NotBlank(message = "validation.error.username.required")
    val username: String = request.getFormParameterValueOrError("username")
}

class Get(request: Request, userPreferenceManager: UserPreferenceManager, contextAttributeName: String)
{
    var register: Boolean = request.queryParameterNames.contains(contextAttributeName)
    val preferredUserName: String? = userPreferenceManager.username
}
