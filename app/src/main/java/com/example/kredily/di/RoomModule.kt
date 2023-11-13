package com.example.kredily.di

import android.app.Application
import com.example.kredily.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDatabase(application: Application): AppDatabase =
        AppDatabase.getInstance(application)

}