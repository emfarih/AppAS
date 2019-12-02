package ml.farih.appas.di

import ml.farih.appas.data.ApiService
import ml.farih.appas.data.Repository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideRepository(apiService: ApiService): Repository =
        Repository(apiService)
}