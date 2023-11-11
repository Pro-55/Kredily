package com.example.kredily.di

import android.content.SharedPreferences
import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.data.repository.impl.KredilyRepositoryImpl
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
        sp: SharedPreferences
    ): KredilyRepository = KredilyRepositoryImpl(sp)

}