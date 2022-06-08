package com.example.imagegallery.di

import com.example.imagegallery.data.ApiInterface
import com.example.imagegallery.data.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiServices(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Provides
    @Singleton
    fun provideRepository(api: ApiInterface): Repository {
        return Repository(
            api
        )
    }
}