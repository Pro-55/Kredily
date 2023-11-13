package com.example.kredily.data.use_case

import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.HomeScreenState
import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

class SearchEmployeeUseCase(
    private val repository: KredilyRepository
) {
    operator fun invoke(
        state: HomeScreenState?,
        searchQuery: String
    ): Flow<Resource<HomeScreenState>> = repository.searchEmployee(
        state = state,
        searchQuery = searchQuery
    )
}