package com.example.kredily.data.use_case

import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Filter
import com.example.kredily.model.HomeScreenState
import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

class AddEmployeeFilterUseCase(
    private val repository: KredilyRepository
) {
    operator fun invoke(
        state: HomeScreenState?,
        filter: Filter
    ): Flow<Resource<HomeScreenState>> = repository.addEmployeeFilter(
        state = state,
        filter = filter
    )
}