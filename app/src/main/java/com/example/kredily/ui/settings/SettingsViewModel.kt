package com.example.kredily.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kredily.data.use_case.LogOutUseCase
import com.example.kredily.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {

    // Global
    private val TAG = SettingsViewModel::class.java.simpleName
    private val _logOutResponse = MutableLiveData<Resource<Boolean>>()
    val logOutResponse: LiveData<Resource<Boolean>> = _logOutResponse

    fun logOut() {
        logOutUseCase()
            .onEach { _logOutResponse.postValue(it) }
            .launchIn(viewModelScope)
    }
}