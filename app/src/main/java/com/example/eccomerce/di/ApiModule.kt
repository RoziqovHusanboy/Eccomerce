package com.example.eccomerce.di

import com.example.eccomerce.data.api.auth.AuthApi
import com.example.eccomerce.data.api.products.ProductApi
import com.example.eccomerce.data.api.order.api.OrderApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit) = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit) = retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun provideOrderApi(retrofit: Retrofit) = retrofit.create(OrderApi::class.java)
}