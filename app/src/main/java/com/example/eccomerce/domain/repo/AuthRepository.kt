package com.example.eccomerce.domain.repo

interface AuthRepository {
    suspend fun signIn(username:String,password:String)
}