package com.example.eccomerce.di

import com.example.eccomerce.data.api.auth.AuthApi
import com.example.eccomerce.data.repo.AuthRepoImpl
import com.example.eccomerce.data.repo.OrderRepositoryImpl
import com.example.eccomerce.data.repo.ProductRepositoryImpl
import com.example.eccomerce.domain.repo.AuthRepository
import com.example.eccomerce.domain.repo.OrderRepository
import com.example.eccomerce.domain.repo.ProductRepository
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

    @Binds
    abstract  fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    abstract  fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

 }