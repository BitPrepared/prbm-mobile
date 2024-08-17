package it.bitprepared.prbm.mobile.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.bitprepared.prbm.mobile.model.Prbm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListPrbmViewModel : ViewModel() {
    private val _modelState = MutableStateFlow(ListPrbmViewModelState())
    val modelState: StateFlow<ListPrbmViewModelState> = _modelState.asStateFlow()

    fun editPrbm(prbm: Prbm) = viewModelScope.launch {
        UserData.prbm = prbm
        _modelState.emit(ListPrbmViewModelState(prbmToEdit = prbm))
    }

    fun editPrbmStarted() = viewModelScope.launch {
        _modelState.emit(ListPrbmViewModelState(prbmToEdit = null))
    }

    fun deletePrbm(context: Context, prbm: Prbm) = viewModelScope.launch {
        UserData.deletePrbm(context, prbm)
        _modelState.emit(ListPrbmViewModelState())
    }

    fun onResume() = viewModelScope.launch {
        _modelState.emit(_modelState.value.copy(lastUpdated = System.currentTimeMillis()))
    }
}

