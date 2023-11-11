package com.example.kredily.di

import android.app.Application
import android.content.res.AssetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AssetManagerModule {

    @Singleton
    @Provides
    fun provideAssetManager(application: Application): AssetManager = application.assets
}