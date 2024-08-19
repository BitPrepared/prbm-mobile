package it.bitprepared.prbm.mobile.activity

import android.content.Context
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

  fun editPrbm() = viewModelScope.launch {
    UserData.prbm = _modelState.value.prbm
    UserData.editPrbm = true
    _modelState.emit(_modelState.value.copy(editReady = true))
  }

  fun editPrbmStarted() = viewModelScope.launch {
    _modelState.emit(_modelState.value.copy(editReady = false))
  }

  fun updateMeters(context: Context, unit: PrbmUnit, newValue: String) = viewModelScope.launch {
    if (newValue.toIntOrNull() != null) {
      unit.meters = newValue.toInt()
      UserData.savePrbm(context, _modelState.value.prbm)
      val index = requireNotNull(UserData.prbm).units.indexOf(unit)
      _modelState.emit(
        _modelState.value.copy(
          lastUpdated = now(), rowEdited = index
        )
      )
    }
  }

  fun updateAzimuth(context: Context, unit: PrbmUnit, newValue: String) = viewModelScope.launch {
    if (newValue.toIntOrNull() != null) {
      unit.azimuth = newValue.toInt()
      UserData.savePrbm(context, _modelState.value.prbm)
      val index = requireNotNull(UserData.prbm).units.indexOf(unit)
      _modelState.emit(
        _modelState.value.copy(
          lastUpdated = now(), rowEdited = index
        )
      )
    }
  }

  fun updateMinutes(context: Context, unit: PrbmUnit, newValue: String) = viewModelScope.launch {
    if (newValue.toIntOrNull() != null) {
      unit.minutes = newValue.toInt()
      UserData.savePrbm(context, _modelState.value.prbm)
      val index = requireNotNull(UserData.prbm).units.indexOf(unit)
      _modelState.emit(
        _modelState.value.copy(
          lastUpdated = now(), rowEdited = index
        )
      )
    }
  }

  fun addUnitAbove(context: Context, unit: PrbmUnit) = viewModelScope.launch {
    val idx = requireNotNull(UserData.prbm).units.indexOf(unit)
    val newUnit = PrbmUnit()
    requireNotNull(UserData.prbm).units.add(idx, newUnit)
    UserData.savePrbm(context, _modelState.value.prbm)
    _modelState.emit(_modelState.value.copy(lastUpdated = now(), rowInserted = idx))
    if (_modelState.value.gpsStatus == GpsStatus.FIXED) {
      updateGpsCoordinatesForUnit(context, newUnit)
    }
  }

  fun deleteUnit(context: Context, unit: PrbmUnit) = viewModelScope.launch {
    if (UserData.prbm!!.units.size > 1) {
      val idx = requireNotNull(UserData.prbm).units.indexOf(unit)
      UserData.prbm!!.units.removeAt(idx)
      UserData.savePrbm(context, _modelState.value.prbm)
      _modelState.emit(_modelState.value.copy(lastUpdated = now(), rowDeleted = idx))
    } else {
      _modelState.emit(_modelState.value.copy(errorMessage = R.string.you_cant_delete_last_unit))
    }
  }

  fun addNewEntity(context: Context, unit: PrbmUnit, columnIndex: Int, selectedMenuIndex: Int) =
    viewModelScope.launch {
      val newEntity = UserData.newEntityFromMenuIndex(context, selectedMenuIndex)
      UserData.entity = newEntity
      UserData.column = columnIndex
      UserData.editEntity = false
      UserData.unit = unit
      UserData.savePrbm(context, _modelState.value.prbm)
      val index = requireNotNull(UserData.prbm).units.indexOf(unit)
      _modelState.emit(_modelState.value.copy(editUnitReady = true, rowEdited = index))
    }

  fun editEntity(context: Context, unit: PrbmUnit, entity: PrbmEntity, columnIndex: Int) =
    viewModelScope.launch {
      UserData.entity = entity
      UserData.column = columnIndex
      UserData.editEntity = true
      UserData.unit = unit
      UserData.savePrbm(context, _modelState.value.prbm)
      val index = requireNotNull(UserData.prbm).units.indexOf(unit)
      _modelState.emit(_modelState.value.copy(editUnitReady = true, rowEdited = index))
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
    val message = when (newStatus) {
      GpsStatus.PAIRING -> R.string.gps_pairing
      else -> R.string.gps_disabled
    }
    _modelState.emit(_modelState.value.copy(gpsStatus = newStatus, errorMessage = message))
  }

  fun updateGpsCoordinates(
    context: Context, latitude: Double, longitude: Double, time: Long, bearing: Float, speed: Float
  ) = viewModelScope.launch {
    requireNotNull(UserData.prbm).coordinates.add(
      PrbmCoordinates(latitude, longitude, time, bearing, speed)
    )
    val units = requireNotNull(UserData.prbm).units
    if (units.size == 1 && !units[0].hasCoordinates()) {
      updateGpsCoordinatesForUnit(context, units[0])
      _modelState.emit(
        _modelState.value.copy(
          lastUpdated = now(), rowEdited = 0
        )
      )
    }
    if (_modelState.value.gpsStatus == GpsStatus.PAIRING) {
      _modelState.emit(
        _modelState.value.copy(
          errorMessage = R.string.gps_paired, gpsStatus = GpsStatus.FIXED
        )
      )
    }
  }

  fun updateGpsCoordinatesForUnit(context: Context, unit: PrbmUnit) = viewModelScope.launch {
    val index = requireNotNull(UserData.prbm).units.indexOf(unit)
    if (_modelState.value.gpsStatus == GpsStatus.FIXED || UserData.prbm?.coordinates?.isNotEmpty() == true) {
      val lastCoordinates = UserData.prbm!!.coordinates.last()
      unit.setCoordinatesFrom(lastCoordinates)
      UserData.savePrbm(context, _modelState.value.prbm)
      _modelState.emit(_modelState.value.copy(lastUpdated = now(), rowEdited = index)
      )
    } else {
      _modelState.emit(_modelState.value.copy(errorMessage = R.string.no_gps_coordinates))
    }
  }

  fun removeGpsCoordinatesForUnit(context: Context, unit: PrbmUnit) = viewModelScope.launch {
    val index = requireNotNull(UserData.prbm).units.indexOf(unit)
    unit.setCoordinatesFrom(PrbmCoordinates())
    UserData.savePrbm(context, _modelState.value.prbm)
    _modelState.emit(_modelState.value.copy(lastUpdated = now(), rowEdited = index))
  }

  fun onResume(context: Context) {
    UserData.savePrbm(context, _modelState.value.prbm)
  }

  fun listAnimationDispatched() = viewModelScope.launch {
    _modelState.emit(
      _modelState.value.copy(
        rowDeleted = null, rowEdited = null, rowInserted = null
      )
    )
  }

  private fun now() = System.currentTimeMillis()

}
