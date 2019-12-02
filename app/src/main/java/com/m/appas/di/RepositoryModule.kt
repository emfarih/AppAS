package com.m.appas.di

import com.m.appas.data.ApiService
import com.m.appas.data.Repository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideRepository(apiService: ApiService): Repository = Repository(apiService)
}