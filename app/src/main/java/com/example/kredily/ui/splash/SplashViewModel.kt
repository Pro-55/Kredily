package com.example.kredily.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kredily.data.use_case.GetLoginStatusUseCase
import com.example.kredily.model.LoginStatus
import com.example.kredily.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getLoginStatusUseCase: GetLoginStatusUseCase
) : ViewModel() {

    // Global
    private val TAG = SplashViewModel::class.java.simpleName
    private val _loginStatus = MutableLiveData<Resource<LoginStatus>>()
    val loginStatus: LiveData<Resource<LoginStatus>> = _loginStatus

    fun getLoginStatus() {
        getLoginStatusUseCase()
            .onEach { _loginStatus.postValue(it) }
            .launchIn(viewModelScope)
    }

}