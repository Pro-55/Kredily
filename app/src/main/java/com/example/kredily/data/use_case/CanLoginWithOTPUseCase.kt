package com.example.kredily.data.use_case

import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Contact
import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

class CanLoginWithOTPUseCase(
    private val repository: KredilyRepository
) {
    operator fun invoke(
        email: String
    ): Flow<Resource<Contact>> = repository.canLoginWithOTP(email = email)
}