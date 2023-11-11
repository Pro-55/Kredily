package com.example.kredily.data.repository.contract

import com.example.kredily.model.Contact
import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

interface KredilyRepository {
    fun getLoginStatus(): Flow<Resource<Boolean>>
    fun login(email: String, password: String): Flow<Resource<Boolean>>
    fun canLoginWithOTP(email: String): Flow<Resource<Contact>>
    fun verifyOTP(email: String, otp: String): Flow<Resource<Boolean>>
}