package com.example.eccomerce.data.repo

import com.example.eccomerce.data.api.auth.AuthApi
import com.example.eccomerce.data.api.dto.SignInRequest
import com.example.eccomerce.data.store.TokenStore
import com.example.eccomerce.data.store.UserStore
import com.example.eccomerce.domain.model.User
import com.example.eccomerce.domain.repo.AuthRepository
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore,
    private val userStore: UserStore
):AuthRepository {

    override suspend fun signIn(username: String, password: String)  {
        val request = SignInRequest(username,password)
        val response = authApi.signIn(request)
        tokenStore.set(response.token)
        userStore.set(response.user)

    }
}