package com.example.kredily.data.use_case

import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

class LogOutUseCase(
    private val repository: KredilyRepository
) {
    operator fun invoke(): Flow<Resource<Boolean>> = repository.logOut()
}