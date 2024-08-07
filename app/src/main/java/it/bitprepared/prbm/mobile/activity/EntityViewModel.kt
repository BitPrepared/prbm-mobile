package it.bitprepared.prbm.mobile.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class EntityViewModel : ViewModel() {

    private val _modelState = MutableStateFlow(getInitialState())
    val modelState: StateFlow<EntityViewModelState> = _modelState.asStateFlow()

    private fun getInitialState(): EntityViewModelState =
        EntityViewModelState(
            typeDescription = UserData.entity?.type?.name ?: "",
            time = UserData.entity?.time ?: "",
            title = UserData.entity?.title ?: "",
            description = UserData.entity?.description ?: "",
            fields = UserData.entity?.type?.fields ?: emptyList(),
            fieldValues = UserData.entity?.fieldValues ?: emptyMap()
        )

    fun updateTitle(newTitle: String) = viewModelScope.launch {
        _modelState.emit(_modelState.value.copy(title = newTitle))
    }

    fun updateDescription(newDescription: String) = viewModelScope.launch {
        _modelState.emit(_modelState.value.copy(description = newDescription))
    }

    fun updateTime(hour: Int, minute: Int) = viewModelScope.launch {
        val newTime = "${String.format(Locale.getDefault(), "%02d", hour)}:${String.format(Locale.getDefault(), "%02d", minute)}"
        _modelState.emit(_modelState.value.copy(time = newTime))
    }

    fun saveEntity() = viewModelScope.launch {
        UserData.entity?.time = _modelState.value.time
        UserData.entity?.title = _modelState.value.title
        UserData.entity?.description = _modelState.value.description
        UserData.entity?.fieldValues?.clear()
        UserData.entity?.fieldValues?.putAll(_modelState.value.fieldValues)
        _modelState.emit(_modelState.value.copy(saveReady = true))
    }

    fun updateFieldValue(fieldName: String, fieldValue: String) = viewModelScope.launch {
        val newFieldValues = _modelState.value.fieldValues.toMutableMap()
        newFieldValues[fieldName] = fieldValue
        _modelState.emit(_modelState.value.copy(fieldValues = newFieldValues))
    }
}
