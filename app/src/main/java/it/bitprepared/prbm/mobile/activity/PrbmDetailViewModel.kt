package it.bitprepared.prbm.mobile.activity

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.PrbmCoordinates
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
        val newUnit = PrbmUnit()
        requireNotNull(UserData.prbm).units.add(idx + 1, newUnit)
        if (_modelState.value.gpsStatus == GpsStatus.FIXED) {
            updateGpsCoordinatesForUnit(newUnit)
        } else {
            _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
        }
    }

    fun deleteUnit(unit: PrbmUnit) = viewModelScope.launch {
        if (UserData.prbm!!.units.size > 1) {
            UserData.prbm!!.units.remove(unit)
            _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
        } else {
            _modelState.emit(_modelState.value.copy(errorMessage = R.string.you_cant_delete_last_unit))
        }
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

    fun errorShown() = viewModelScope.launch {
        _modelState.emit(_modelState.value.copy(errorMessage = null))
    }

    fun userToggledGps() = viewModelScope.launch {
        val newStatus = when (_modelState.value.gpsStatus) {
            GpsStatus.DISABLED -> GpsStatus.PAIRING
            else -> GpsStatus.DISABLED
        }
        _modelState.emit(_modelState.value.copy(gpsStatus = newStatus))
    }

    fun updateGpsCoordinates(
        latitude: Double,
        longitude: Double,
        time: Long,
        bearing: Float,
        speed: Float
    ) = viewModelScope.launch {
        requireNotNull(UserData.prbm).coordinates.add(
            PrbmCoordinates(latitude, longitude, time, bearing, speed)
        )
        val units = requireNotNull(UserData.prbm).units
        if (units.size == 1 && !units[0].hasCoordinates()) {
            updateGpsCoordinatesForUnit(units[0])
        }
        if (_modelState.value.gpsStatus == GpsStatus.PAIRING) {
            _modelState.emit(_modelState.value.copy(
                errorMessage = R.string.gps_paired,
                gpsStatus = GpsStatus.FIXED
            ))
        }
    }

    fun updateGpsCoordinatesForUnit(unit: PrbmUnit) = viewModelScope.launch {
        if (_modelState.value.gpsStatus == GpsStatus.FIXED || UserData.prbm?.coordinates?.isNotEmpty() == true) {
            val lastCoordinates = UserData.prbm!!.coordinates.last()
            unit.setCoordinatesFrom(lastCoordinates)
            _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
        } else {
            _modelState.emit(_modelState.value.copy(errorMessage = R.string.no_gps_coordinates))
        }
    }

    fun removeGpsCoordinatesForUnit(unit: PrbmUnit) = viewModelScope.launch {
        unit.setCoordinatesFrom(PrbmCoordinates())
        _modelState.emit(_modelState.value.copy(stateTimestamp = System.currentTimeMillis()))
    }
}
