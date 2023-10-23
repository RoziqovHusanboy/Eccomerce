package com.example.eccomerce.data.api.auth

import com.example.eccomerce.data.api.auth.dto.SignInRequest
import com.example.eccomerce.data.api.auth.dto.AuthResponce
import com.example.eccomerce.data.api.auth.dto.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/sign-in")
    suspend fun signIn(@Body request: SignInRequest): AuthResponce

    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignUpRequest):AuthResponce

}