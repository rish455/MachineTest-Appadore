package com.appadore.machinetest.di

import android.content.Context
import com.appadore.machinetest.App
import com.appadore.machinetest.data.local.DataStoreHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApplication(@ApplicationContext app: Context): App {
        return app as App
    }

    @Provides
    fun provideContext(@ApplicationContext app: Context): Context {
        return app
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStoreHelper {
        return DataStoreHelper(context)
    }
}