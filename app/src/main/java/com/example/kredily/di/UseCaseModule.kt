package com.example.kredily.di

import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.data.use_case.AddEmployeeFilterUseCase
import com.example.kredily.data.use_case.CanLoginWithOTPUseCase
import com.example.kredily.data.use_case.ClearAllEmployeeFiltersUseCase
import com.example.kredily.data.use_case.FetchEmployeesUseCase
import com.example.kredily.data.use_case.GetEmployeesUseCase
import com.example.kredily.data.use_case.GetHomeScreenStateUseCase
import com.example.kredily.data.use_case.GetLoginStatusUseCase
import com.example.kredily.data.use_case.GetOfficeLocationsUseCase
import com.example.kredily.data.use_case.LoginUseCase
import com.example.kredily.data.use_case.RemoveEmployeeFilterUseCase
import com.example.kredily.data.use_case.SearchEmployeeUseCase
import com.example.kredily.data.use_case.SetPasscodeUseCase
import com.example.kredily.data.use_case.VerifyOTPUseCase
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

    @ViewModelScoped
    @Provides
    fun provideLoginUseCase(
        repository: KredilyRepository
    ): LoginUseCase = LoginUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideCanLoginWithOTPUseCase(
        repository: KredilyRepository
    ): CanLoginWithOTPUseCase = CanLoginWithOTPUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideVerifyOTPUseCase(
        repository: KredilyRepository
    ): VerifyOTPUseCase = VerifyOTPUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideGetOfficeLocationsUseCase(
        repository: KredilyRepository
    ): GetOfficeLocationsUseCase = GetOfficeLocationsUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideSetPasscodeUseCase(
        repository: KredilyRepository
    ): SetPasscodeUseCase = SetPasscodeUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideFetchEmployeesUseCase(
        repository: KredilyRepository
    ): FetchEmployeesUseCase = FetchEmployeesUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideGetEmployeesUseCase(
        repository: KredilyRepository
    ): GetEmployeesUseCase = GetEmployeesUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideUpdateHomeScreenStateUseCase(
        repository: KredilyRepository
    ): GetHomeScreenStateUseCase = GetHomeScreenStateUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideSearchEmployeeUseCase(
        repository: KredilyRepository
    ): SearchEmployeeUseCase = SearchEmployeeUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideAddEmployeeFilterUseCase(
        repository: KredilyRepository
    ): AddEmployeeFilterUseCase = AddEmployeeFilterUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideRemoveEmployeeFilterUseCase(
        repository: KredilyRepository
    ): RemoveEmployeeFilterUseCase = RemoveEmployeeFilterUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideClearAllEmployeeFiltersUseCase(
        repository: KredilyRepository
    ): ClearAllEmployeeFiltersUseCase = ClearAllEmployeeFiltersUseCase(repository)
}