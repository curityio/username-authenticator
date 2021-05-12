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

package io.curity.identityserver.plugin.username.authentication

import io.curity.identityserver.plugin.username.config.UsernameAuthenticatorPluginConfig
import se.curity.identityserver.sdk.attribute.Attribute
import se.curity.identityserver.sdk.attribute.Attributes
import se.curity.identityserver.sdk.attribute.AuthenticationAttributes
import se.curity.identityserver.sdk.attribute.ContextAttributes
import se.curity.identityserver.sdk.attribute.SubjectAttributes
import se.curity.identityserver.sdk.authentication.AuthenticationResult
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler
import se.curity.identityserver.sdk.errors.ErrorCode
import se.curity.identityserver.sdk.web.Request
import se.curity.identityserver.sdk.web.Response
import se.curity.identityserver.sdk.web.ResponseModel.templateResponseModel
import java.util.Date
import java.util.Optional

class UsernameAuthenticatorRequestHandler(config: UsernameAuthenticatorPluginConfig)
    : AuthenticatorRequestHandler<RequestModel>
{

    private val userPreferencesManager = config.userPreferencesManager
    private val autoPostLoginHint = config.autoSubmitPreferredUserName()
    private val exceptionFactory = config.exceptionFactory

    override fun get(requestModel: RequestModel, response: Response): Optional<AuthenticationResult>
    {
        if (autoPostLoginHint && requestModel.getRequestModel?.preferredUserName != null)
        {
            return Optional.of(createAuthenticationResult(requestModel.getRequestModel.preferredUserName))
        }
        return Optional.empty()
    }


    override fun post(requestModel: RequestModel, response: Response): Optional<AuthenticationResult>
    {
        val postRequestModel = requestModel.postRequestModel
                ?: throw exceptionFactory.internalServerException(ErrorCode.GENERIC_ERROR,
                        "Could not find correct request model")

        userPreferencesManager.saveUsername(postRequestModel.username)

        return Optional.of(createAuthenticationResult(postRequestModel.username))
    }

    private fun createAuthenticationResult(userName: String) = AuthenticationResult(
            AuthenticationAttributes.of(
                    SubjectAttributes.of(userName, Attributes.of(Attribute.of("username", userName))),
                    ContextAttributes.of(Attributes.of(Attribute.of("iat", Date().time)))))

    companion object
    {
        const val templateName = "authenticate/get"
    }

    override fun preProcess(request: Request, response: Response): RequestModel
    {
        if (request.isGetRequest)
        {
            // GET request
            response.putViewData("username", userPreferencesManager.username,
                    Response.ResponseModelScope.NOT_FAILURE)
        }

        // on request validation failure, we should use the same template as for NOT_FAILURE
        response.setResponseModel(templateResponseModel(emptyMap<String, Any>(),
                templateName), Response.ResponseModelScope.ANY)

        return RequestModel(request, userPreferencesManager)
    }
}