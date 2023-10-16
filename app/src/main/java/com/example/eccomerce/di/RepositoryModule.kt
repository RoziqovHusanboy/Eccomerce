package com.example.eccomerce.di

import com.example.eccomerce.data.api.auth.AuthApi
import com.example.eccomerce.data.repo.AuthRepoImpl
import com.example.eccomerce.domain.repo.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  abstract  fun bindAuthRepository(
        authRepoImpl: AuthRepoImpl
    ): AuthRepository

 }