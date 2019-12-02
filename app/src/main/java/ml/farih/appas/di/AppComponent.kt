package ml.farih.appas.di

import ml.farih.appas.ui.activity.MainActivity
import ml.farih.appas.ui.provider.MainSliceProvider
import dagger.Component

@Component(modules = [RetrofitModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(mainSliceProvider: MainSliceProvider)
    fun inject(mainSliceProvider: MainActivity)
}