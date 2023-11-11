package com.example.kredily.data.use_case

import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

class LoginUseCase(
    private val repository: KredilyRepository
) {
    operator fun invoke(
        email: String,
        password: String
    ): Flow<Resource<Boolean>> = repository.login(
        email = email,
        password = password
    )
}