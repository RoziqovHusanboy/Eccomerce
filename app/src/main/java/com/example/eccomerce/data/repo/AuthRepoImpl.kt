package com.example.eccomerce.data.repo

import com.example.eccomerce.data.api.auth.AuthApi
import com.example.eccomerce.data.api.auth.dto.AuthResponce
import com.example.eccomerce.data.api.auth.dto.SignInRequest
import com.example.eccomerce.data.api.auth.dto.SignUpRequest
import com.example.eccomerce.data.store.OnboardedStore
import com.example.eccomerce.data.store.TokenStore
import com.example.eccomerce.data.store.UserStore
import com.example.eccomerce.domain.model.Destination
import com.example.eccomerce.domain.repo.AuthRepository
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore,
    private val userStore: UserStore,
    private val onboardedStore: OnboardedStore
) : AuthRepository {

    override suspend fun signIn(username: String, password: String) {
        val request = SignInRequest(username, password)
        val response = authApi.signIn(request)
        saveUserInfo(response)
    }

    override suspend fun signUp(username: String, email: String, password: String) {
        val request = SignUpRequest(username, email, password)
        val response = authApi.signUp(request)
        saveUserInfo(response)
    }

    override fun destinationFlow() = channelFlow {
        suspend fun sendDestination() {
            when {
                tokenStore.get() != null -> send(Destination.Home)
                onboardedStore.get() == true -> send(Destination.Auth)
                else -> send(Destination.Onboarding)
            }
        }

        launch {
            tokenStore.getFlow().collectLatest {
                sendDestination()
            }
        }

        launch {
            onboardedStore.getFlow().collectLatest {
                sendDestination()
            }
        }

    }.distinctUntilChanged()

    override suspend fun onboarded()  = onboardedStore.set(true)


    private suspend fun saveUserInfo(response: AuthResponce) {
        tokenStore.set(response.token)
        userStore.set(response.user)
    }
}