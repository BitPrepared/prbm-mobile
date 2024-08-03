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

    fun updateMeters(prbmUnit: PrbmUnit, newValue: String) = viewModelScope.launch {
        if (newValue.toIntOrNull() != null) {
            prbmUnit.meters = newValue.toInt()
            val editedPrbm = requireNotNull(UserData.prbm).units.indexOf(prbmUnit)
            _modelState.emit(_modelState.value.copy(editedRow = editedPrbm))
        }
    }

    fun updateAzimuth(prbmUnit: PrbmUnit, newValue: String) = viewModelScope.launch {
        if (newValue.toIntOrNull() != null) {
            prbmUnit.azimuth = newValue.toInt()
            val editedPrbm = requireNotNull(UserData.prbm).units.indexOf(prbmUnit)
            _modelState.emit(_modelState.value.copy(editedRow = editedPrbm))
        }
    }

    fun updateMinutes(prbmUnit: PrbmUnit, newValue: String) = viewModelScope.launch {
        if (newValue.toIntOrNull() != null) {
            prbmUnit.minutes = newValue.toInt()
            val editedPrbm = requireNotNull(UserData.prbm).units.indexOf(prbmUnit)
            _modelState.emit(_modelState.value.copy(editedRow = editedPrbm))
        }
    }

    fun listUpdateDone() = viewModelScope.launch {
        _modelState.emit(_modelState.value.copy(editedRow = null, addedRow = null))
    }

    fun addUnitFromPlusPosition(position: Int) = viewModelScope.launch {
        requireNotNull(UserData.prbm).units.add(position, PrbmUnit())
        _modelState.emit(_modelState.value.copy(addedRow = position))
    }
}