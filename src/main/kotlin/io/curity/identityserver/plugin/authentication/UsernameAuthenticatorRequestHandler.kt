/*
 *  Copyright 2017 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.curity.identityserver.plugin.authentication

import io.curity.identityserver.plugin.config.UsernameAuthenticatorPluginConfig
import se.curity.identityserver.sdk.attribute.*
import se.curity.identityserver.sdk.authentication.AuthenticationResult
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler
import se.curity.identityserver.sdk.http.HttpStatus
import se.curity.identityserver.sdk.web.Request
import se.curity.identityserver.sdk.web.Response
import se.curity.identityserver.sdk.web.ResponseModel.templateResponseModel
import java.util.*
import java.util.Collections.emptyMap

class UsernameAuthenticatorRequestHandler(config: UsernameAuthenticatorPluginConfig)
    : AuthenticatorRequestHandler<RequestModel> {

    private val userPreferencesManager = config.userPreferencesManager

    override fun get(requestModel: RequestModel, response: Response): Optional<AuthenticationResult> {
        return Optional.empty()
    }

    override fun post(requestModel: RequestModel, response: Response): Optional<AuthenticationResult> {
        return Optional.of(
                AuthenticationResult(
                        AuthenticationAttributes.of(
                                SubjectAttributes.of(requestModel.username, Attributes.of(Attribute.of("username", requestModel.username))),
                                ContextAttributes.of(Attributes.of(Attribute.of("iat", Date().time))))))
    }

    override fun preProcess(request: Request, response: Response): RequestModel {
        if (request.isGetRequest) {
            // GET request
            Collections.singletonMap("username", userPreferencesManager.username)
            response.setResponseModel(templateResponseModel(Collections.singletonMap("username", userPreferencesManager.username) as Map<String, Any>?, "authenticate/get"),
                    Response.ResponseModelScope.NOT_FAILURE)
        }

        // on request validation failure, we should use the same template as for NOT_FAILURE
        response.setResponseModel(templateResponseModel(emptyMap<String, Any>(),
                "authenticate/get"), HttpStatus.BAD_REQUEST)

        return RequestModel(request)
    }
}