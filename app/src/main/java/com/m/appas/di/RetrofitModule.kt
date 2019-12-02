package com.m.appas.di

import com.m.appas.data.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RetrofitModule {
    @Provides
    fun provideApiService(): ApiService {
        val retrofit =  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://appas-99f6.restdb.io/rest/")
            .build()
        return retrofit.create(ApiService::class.java)
    }
}