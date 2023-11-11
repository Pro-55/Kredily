package com.example.kredily.di

import android.content.SharedPreferences
import android.content.res.AssetManager
import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.data.repository.impl.KredilyRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideKredilyRepository(
        sp: SharedPreferences,
        am: AssetManager,
        gson: Gson
    ): KredilyRepository = KredilyRepositoryImpl(sp, am, gson)

}