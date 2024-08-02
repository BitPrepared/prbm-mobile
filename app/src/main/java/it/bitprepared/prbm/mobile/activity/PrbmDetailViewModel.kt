package it.bitprepared.prbm.mobile.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class PrbmDetailViewModel : ViewModel() {

    private val _modelState = MutableStateFlow(PrbmDetailModelState(UserData.prbm!!))
    val modelState: StateFlow<PrbmDetailModelState> = _modelState.asStateFlow()

    fun savePrbm(context: Context) = viewModelScope.launch {
        UserData.savePrbm(context, _modelState.value.prbm)
        _modelState.emit(_modelState.value.copy(saveSuccessful = true))
    }

    fun editPrbm() = viewModelScope.launch {
        UserData.prbm = _modelState.value.prbm
        UserData.editPrbm = true
        _modelState.emit(_modelState.value.copy(editReady = true))
    }

    fun editPrbmStarted() = viewModelScope.launch {
        _modelState.emit(_modelState.value.copy(editReady = false))
    }

}