package it.bitprepared.prbm.mobile.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.bitprepared.prbm.mobile.model.PrbmUnit
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

    fun showSavedDone() = viewModelScope.launch {
        _modelState.emit(_modelState.value.copy(saveSuccessful = false))
    }

    fun updateMeters(position: Int, newValue: String) = viewModelScope.launch {
        if (newValue.toIntOrNull() != null) {
            UserData.prbm!!.units[position].meters = newValue.toInt()
            _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
        }
    }

    fun updateAzimuth(position: Int, newValue: String) = viewModelScope.launch {
        if (newValue.toIntOrNull() != null) {
            UserData.prbm!!.units[position].azimuth = newValue.toInt()
            _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
        }
    }

    fun updateMinutes(position: Int, newValue: String) = viewModelScope.launch {
        if (newValue.toIntOrNull() != null) {
            UserData.prbm!!.units[position].minutes = newValue.toInt()
            _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
        }
    }

    fun addUnitFromPlusPosition(position: Int) = viewModelScope.launch {
        requireNotNull(UserData.prbm).units.add(position, PrbmUnit())
        _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
    }

    fun deleteUnit(position: Int) = viewModelScope.launch {
        UserData.prbm!!.units.removeAt(position)
        _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
    }
}