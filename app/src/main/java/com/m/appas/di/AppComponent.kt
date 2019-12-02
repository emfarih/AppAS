package com.m.appas.di

import com.m.appas.ui.activity.MainActivity
import com.m.appas.ui.provider.MainSliceProvider
import dagger.Component

@Component(modules = [RetrofitModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(mainSliceProvider: MainSliceProvider)
    fun inject(mainSliceProvider: MainActivity)
}