package com.example.eccomerce.domain.repo

import com.example.eccomerce.domain.model.Destination
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(username:String,password:String)
    suspend fun signUp(username: String,email:String,password: String)

    fun destinationFlow():Flow<Destination>
    suspend fun onboarded()
}