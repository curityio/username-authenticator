package io.curity.identityserver.plugin.username.authentication

import org.hibernate.validator.constraints.NotBlank
import se.curity.identityserver.sdk.web.Request
import javax.validation.Valid

class RequestModel(request: Request) {
    @Valid
    val postRequestModel: Post? = if (request.isPostRequest) Post(request)
    else
        null
}

class Post(request: Request) {
    @NotBlank(message = "validation.error.username.required")
    val username: String = request.getFormParameterValueOrError("username")
}
