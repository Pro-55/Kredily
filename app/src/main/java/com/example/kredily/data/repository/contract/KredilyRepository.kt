package com.example.kredily.data.repository.contract

import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

interface KredilyRepository {
    fun getLoginStatus(): Flow<Resource<Boolean>>
}