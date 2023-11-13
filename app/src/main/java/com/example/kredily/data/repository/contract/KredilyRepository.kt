package com.example.kredily.data.repository.contract

import com.example.kredily.model.Contact
import com.example.kredily.model.Employee
import com.example.kredily.model.Filter
import com.example.kredily.model.HomeScreenState
import com.example.kredily.model.LoginStatus
import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

interface KredilyRepository {
    fun getLoginStatus(): Flow<Resource<LoginStatus>>
    fun login(email: String, password: String): Flow<Resource<Boolean>>
    fun canLoginWithOTP(email: String): Flow<Resource<Contact>>
    fun verifyOTP(email: String, otp: String): Flow<Resource<Boolean>>
    fun getOfficeLocations(): Flow<Resource<List<String>>>
    fun setPasscode(passcode: String?, confirmedPasscode: String?): Flow<Resource<Boolean>>
    fun fetchEmployees(): Flow<Resource<Boolean>>
    fun getEmployees(): Flow<Resource<List<Employee>>>
    fun getHomeScreenState(
        state: HomeScreenState?,
        employees: List<Employee>
    ): Flow<Resource<HomeScreenState>>

    fun searchEmployee(
        state: HomeScreenState?,
        searchQuery: String
    ): Flow<Resource<HomeScreenState>>

    fun addEmployeeFilter(
        state: HomeScreenState?,
        filter: Filter
    ): Flow<Resource<HomeScreenState>>

    fun removeEmployeeFilter(
        state: HomeScreenState?,
        filter: Filter
    ): Flow<Resource<HomeScreenState>>

    fun clearAllEmployeeFilters(state: HomeScreenState?): Flow<Resource<HomeScreenState>>

    fun logOut(): Flow<Resource<Boolean>>
}