package com.example.kredily.data.use_case

import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

class VerifyOTPUseCase(
    private val repository: KredilyRepository
) {
    operator fun invoke(
        email: String,
        otp: String
    ): Flow<Resource<Boolean>> = repository.verifyOTP(email = email, otp = otp)
}