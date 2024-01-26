package io.curity.identityserver.plugin.username.authentication

import org.hibernate.validator.constraints.NotBlank
import se.curity.identityserver.sdk.service.UserPreferenceManager
import se.curity.identityserver.sdk.web.Request
import javax.validation.Valid

class RequestModel(request: Request, userPreferenceManager: UserPreferenceManager)
{
    @Valid
    val postRequestModel: Post? = if (request.isPostRequest) Post(request) else null

    @Valid
    val getRequestModel: Get? = if (request.isGetRequest) Get(userPreferenceManager) else null
}

class Post(request: Request)
{
    @NotBlank(message = "validation.error.username.required")
    val username: String = request.getFormParameterValueOrError("username")
    var register: MutableCollection<String>? = request.getFormParameterValues("register")
}

class Get(userPreferenceManager: UserPreferenceManager)
{
    val preferredUserName: String? = userPreferenceManager.username
}
