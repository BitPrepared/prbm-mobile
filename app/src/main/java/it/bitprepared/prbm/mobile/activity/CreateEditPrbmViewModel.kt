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

class CreateEditPrbmViewModel : ViewModel() {

    private val _modelState = MutableStateFlow(getInitialState())
    val modelState: StateFlow<CreateEditPrbmViewModelState> = _modelState.asStateFlow()

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

    private fun getInitialState(): CreateEditPrbmViewModelState {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        if (UserData.editPrbm) {
            val prbm = requireNotNull(UserData.prbm)
            return CreateEditPrbmViewModelState(
                date = prbm.date ?: "",
                time = prbm.time ?: "",
                title = prbm.title,
                authors = prbm.authors ?: "",
                place = prbm.place ?: "",
                notes = prbm.note ?: "",
                isEditing = true
            )
        } else {
            val now = ZonedDateTime.now()
            return CreateEditPrbmViewModelState(
                date = now.toLocalDate().format(dateFormatter),
                time = now.toLocalTime().format(timeFormatter),
                isEditing = false
            )
        }
    }

    fun savePrbm(context: Context) = viewModelScope.launch {
        if (_modelState.value.title.isBlank()) {
            _modelState.value =
                _modelState.value.copy(errorMessageResId = R.string.error_no_title_prbm)
        } else {
            val newPrbm = if (UserData.editPrbm) {
                requireNotNull(UserData.prbm).copy(
                    title = _modelState.value.title,
                    authors = _modelState.value.authors,
                    place = _modelState.value.place,
                    note = _modelState.value.notes,
                    date = _modelState.value.date,
                    time = _modelState.value.time
                )
            } else {
                Prbm(
                    title = _modelState.value.title,
                    authors = _modelState.value.authors,
                    place = _modelState.value.place,
                    note = _modelState.value.notes,
                    date = _modelState.value.date,
                    time = _modelState.value.time,
                )
            }
            UserData.prbm = newPrbm
            UserData.editPrbm = false
            UserData.savePrbm(context, newPrbm)
            _modelState.value = _modelState.value.copy(saveReady = true)
        }
    }
}
