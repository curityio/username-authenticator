package io.curity.identityserver.plugin.authentication

import org.hibernate.validator.constraints.NotBlank
import se.curity.identityserver.sdk.web.Request

class RequestModel(request: Request) {
    @NotBlank(message = "validation.error.username.required")
    val username: String = request.getFormParameterValueOrError("username")
}