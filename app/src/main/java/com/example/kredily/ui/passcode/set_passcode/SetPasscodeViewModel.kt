package com.example.kredily.ui.passcode.set_passcode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kredily.data.use_case.GetOfficeLocationsUseCase
import com.example.kredily.data.use_case.SetPasscodeUseCase
import com.example.kredily.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SetPasscodeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getOfficeLocationsUseCase: GetOfficeLocationsUseCase,
    private val setPasscodeUseCase: SetPasscodeUseCase
) : ViewModel() {

    // Global
    private val TAG = SetPasscodeViewModel::class.java.simpleName
    private val _officeLocations = MutableLiveData<Resource<List<String>>>()
    val officeLocations: LiveData<Resource<List<String>>> = _officeLocations
    private val _setPasscodeResponse = MutableLiveData<Resource<Boolean>>()
    val setPasscodeResponse: LiveData<Resource<Boolean>> = _setPasscodeResponse

    init {
        getOfficeLocations()
    }

    private fun getOfficeLocations() {
        getOfficeLocationsUseCase()
            .onEach { _officeLocations.postValue(it) }
            .launchIn(viewModelScope)
    }

    fun setPasscode(
        passcode: String?,
        confirmedPasscode: String?
    ) {
        setPasscodeUseCase(
            passcode = passcode,
            confirmedPasscode = confirmedPasscode
        )
            .onEach { _setPasscodeResponse.postValue(it) }
            .launchIn(viewModelScope)
    }

}