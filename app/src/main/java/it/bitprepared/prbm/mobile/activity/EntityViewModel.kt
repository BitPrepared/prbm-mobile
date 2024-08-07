package it.bitprepared.prbm.mobile.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.bitprepared.prbm.mobile.model.PrbmEntity
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
            isEditing = UserData.editEntity,
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
        val involvedEntity = requireNotNull(UserData.entity).apply {
            time = _modelState.value.time
            title = _modelState.value.title
            description = _modelState.value.description
            fieldValues.clear()
            fieldValues.putAll(_modelState.value.fieldValues)
        }
        val involvedUnit = UserData.unit
        if (!UserData.editEntity) {
            when (UserData.column) {
                0 -> involvedUnit?.entitiesFarLeft?.add(involvedEntity)
                1 -> involvedUnit?.entitiesNearLeft?.add(involvedEntity)
                2 -> involvedUnit?.entitiesNearRight?.add(involvedEntity)
                3 -> involvedUnit?.entitiesFarRight?.add(involvedEntity)
            }
        }
        _modelState.emit(_modelState.value.copy(saveReady = true))
    }

    fun deleteEntity() = viewModelScope.launch {
        val involvedEntity = UserData.entity!!
        val involvedUnit = UserData.unit
        when (UserData.column) {
            0 -> involvedUnit?.entitiesFarLeft?.remove(involvedEntity)
            1 -> involvedUnit?.entitiesNearLeft?.remove(involvedEntity)
            2 -> involvedUnit?.entitiesNearRight?.remove(involvedEntity)
            3 -> involvedUnit?.entitiesFarRight?.remove(involvedEntity)
        }
        _modelState.emit(_modelState.value.copy(saveReady = true))
    }

    fun updateFieldValue(fieldName: String, fieldValue: String) = viewModelScope.launch {
        val newFieldValues = _modelState.value.fieldValues.toMutableMap()
        newFieldValues[fieldName] = fieldValue
        _modelState.emit(_modelState.value.copy(fieldValues = newFieldValues))
    }

}
