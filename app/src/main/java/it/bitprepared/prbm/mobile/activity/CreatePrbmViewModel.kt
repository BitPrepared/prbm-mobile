package it.bitprepared.prbm.mobile.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.Prbm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CreatePrbmViewModel : ViewModel() {

    private val _modelState = MutableStateFlow(getInitialState())
    val modelState: StateFlow<CreatePrbmViewModelState> = _modelState.asStateFlow()

    fun updateTitle(newTitle: String) = viewModelScope.launch {
        if (newTitle != _modelState.value.title) {
            _modelState.value = _modelState.value.copy(title = newTitle)
        }
    }

    fun updateAuthors(newAuthors: String) = viewModelScope.launch {
        if (newAuthors != _modelState.value.authors) {
            _modelState.value = _modelState.value.copy(authors = newAuthors)
        }
    }

    fun updatePlace(newPlace: String) = viewModelScope.launch {
        if (newPlace != _modelState.value.place) {
            _modelState.value = _modelState.value.copy(place = newPlace)
        }
    }

    fun updateNotes(newNotes: String) = viewModelScope.launch {
        if (newNotes != _modelState.value.notes) {
            _modelState.value = _modelState.value.copy(notes = newNotes)
        }
    }

    fun updateDate(selection: Long?) = viewModelScope.launch {
        if (selection == null) return@launch
        val time = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(selection), ZoneId.of("UTC")
        )
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        _modelState.value = _modelState.value.copy(date = dateFormatter.format(time))
    }

    fun updateTime(hour: Int, minute: Int) {
        val newTime = LocalTime.of(hour, minute)
        _modelState.value = _modelState.value.copy(time = newTime.toString())
    }

    private fun getInitialState(): CreatePrbmViewModelState {
        val now = ZonedDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        return CreatePrbmViewModelState(
            date = now.toLocalDate().format(dateFormatter),
            time = now.toLocalTime().format(timeFormatter)
        )
    }

    fun savePrbm(context: Context) = viewModelScope.launch {
        if (_modelState.value.title.isBlank()) {
            _modelState.value =
                _modelState.value.copy(errorMessageResId = R.string.error_no_title_prbm)
        } else {
            val newPrbm = Prbm(
                title = _modelState.value.title,
                authors = _modelState.value.authors,
                place = _modelState.value.place,
                note = _modelState.value.notes,
                date = "${_modelState.value.date}T${_modelState.value.time}:00Z"
            )
            UserData.prbm = newPrbm
            UserData.savePrbm(context, newPrbm)
            _modelState.value = _modelState.value.copy(saveReady = true)
        }
    }
}
