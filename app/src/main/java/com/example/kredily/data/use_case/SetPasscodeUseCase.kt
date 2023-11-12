package com.example.kredily.data.use_case

import com.example.kredily.data.repository.contract.KredilyRepository
import com.example.kredily.model.Resource
import kotlinx.coroutines.flow.Flow

class SetPasscodeUseCase(
    private val repository: KredilyRepository
) {
    operator fun invoke(
        passcode: String?,
        confirmedPasscode: String?
    ): Flow<Resource<Boolean>> = repository.setPasscode(
        passcode = passcode,
        confirmedPasscode = confirmedPasscode
    )
}