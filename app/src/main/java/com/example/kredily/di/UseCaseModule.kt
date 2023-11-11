package com.example.kredily.di

import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.data.use_case.GetLoginStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideGetLoginStatusUseCase(
        repository: KredilyRepository
    ): GetLoginStatusUseCase = GetLoginStatusUseCase(repository)

}