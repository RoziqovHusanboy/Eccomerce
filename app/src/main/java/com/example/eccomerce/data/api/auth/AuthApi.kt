package com.example.eccomerce.data.api.auth

import com.example.eccomerce.data.api.dto.SignInRequest
import com.example.eccomerce.data.api.dto.SignInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/sign-in")
    suspend fun signIn(@Body request:SignInRequest):SignInResponse
}