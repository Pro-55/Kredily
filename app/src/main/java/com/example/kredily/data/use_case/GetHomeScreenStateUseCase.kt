package com.example.kredily.data.use_case

import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Employee
import com.example.kredily.model.HomeScreenState
import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

class GetHomeScreenStateUseCase(
    private val repository: KredilyRepository
) {
    operator fun invoke(
        state: HomeScreenState?,
        employees: List<Employee>
    ): Flow<Resource<HomeScreenState>> = repository.getHomeScreenState(
        state = state,
        employees = employees
    )
}