package it.bitprepared.prbm.mobile.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.bitprepared.prbm.mobile.model.PrbmEntity
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

    fun updateMeters(unit: PrbmUnit, newValue: String) = viewModelScope.launch {
        if (newValue.toIntOrNull() != null) {
            unit.meters = newValue.toInt()
            _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
        }
    }

    fun updateAzimuth(unit: PrbmUnit, newValue: String) = viewModelScope.launch {
        if (newValue.toIntOrNull() != null) {
            unit.azimuth = newValue.toInt()
            _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
        }
    }

    fun updateMinutes(unit: PrbmUnit, newValue: String) = viewModelScope.launch {
        if (newValue.toIntOrNull() != null) {
            unit.minutes = newValue.toInt()
            _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
        }
    }

    fun addUnitBelow(unit: PrbmUnit) = viewModelScope.launch {
        val idx = requireNotNull(UserData.prbm).units.indexOf(unit)
        requireNotNull(UserData.prbm).units.add(idx + 1, PrbmUnit())
        _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
    }

    fun deleteUnit(unit: PrbmUnit) = viewModelScope.launch {
        UserData.prbm!!.units.remove(unit)
        _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
    }

    fun addNewEntity(unit: PrbmUnit, columnIndex: Int, selectedMenuIndex: Int) = viewModelScope.launch {
        val newEntity = UserData.newEntityFromMenuIndex(selectedMenuIndex)
        UserData.entity = newEntity
        UserData.column = columnIndex
        UserData.editEntity = false
        UserData.unit = unit
        _modelState.emit(_modelState.value.copy(editUnitReady = true))
    }

    fun editEntity(unit: PrbmUnit, entity: PrbmEntity, columnIndex: Int) = viewModelScope.launch {
        UserData.entity = entity
        UserData.column = columnIndex
        UserData.editEntity = true
        UserData.unit = unit
        _modelState.emit(_modelState.value.copy(editUnitReady = true))
    }

    fun editEntityStarted() = viewModelScope.launch {
        _modelState.emit(_modelState.value.copy(editUnitReady = false))
    }
}
