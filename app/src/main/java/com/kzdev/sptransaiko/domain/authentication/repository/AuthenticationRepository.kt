package com.kzdev.sptransaiko.domain.authentication.repository

import com.kzdev.sptransaiko.domain.authentication.network.AuthenticationService

class AuthenticationRepository(private val authenticationService: AuthenticationService) {
    fun postAuthentication(token: String) =
        authenticationService.postAuthentication(token)
}