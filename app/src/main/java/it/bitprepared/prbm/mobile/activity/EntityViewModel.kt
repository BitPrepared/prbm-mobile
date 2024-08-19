package it.bitprepared.prbm.mobile.activity

import android.util.Log
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
            isEditing = UserData.editEntity,
            typeDescription = UserData.entity?.type?.name ?: "",
            time = UserData.entity?.time ?: "",
            title = UserData.entity?.title ?: "",
            description = UserData.entity?.description ?: "",
            fields = UserData.entity?.type?.fields ?: emptyList(),
            fieldValues = UserData.entity?.fieldValues ?: emptyMap(),
            imageUris = UserData.entity?.pictureUri ?: emptyList(),
            imageFilenames = UserData.entity?.pictureFilenames ?: emptyList(),
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
            populatePictures(_modelState.value.imageUris, _modelState.value.imageFilenames)
        }
        val involvedUnit = UserData.unit
        if (!UserData.editEntity) {
            when (UserData.column) {
                0 -> involvedUnit?.farLeft?.add(involvedEntity)
                1 -> involvedUnit?.nearLeft?.add(involvedEntity)
                2 -> involvedUnit?.nearRight?.add(involvedEntity)
                3 -> involvedUnit?.farRight?.add(involvedEntity)
            }
        }
        _modelState.emit(_modelState.value.copy(saveReady = true))
    }

    fun deleteEntity() = viewModelScope.launch {
        val involvedEntity = UserData.entity!!
        val involvedUnit = UserData.unit
        when (UserData.column) {
            0 -> involvedUnit?.farLeft?.remove(involvedEntity)
            1 -> involvedUnit?.nearLeft?.remove(involvedEntity)
            2 -> involvedUnit?.nearRight?.remove(involvedEntity)
            3 -> involvedUnit?.farRight?.remove(involvedEntity)
        }
        _modelState.emit(_modelState.value.copy(saveReady = true))
    }

    fun updateFieldValue(fieldName: String, fieldValue: String) = viewModelScope.launch {
        val newFieldValues = _modelState.value.fieldValues.toMutableMap()
        newFieldValues[fieldName] = fieldValue
        _modelState.emit(_modelState.value.copy(fieldValues = newFieldValues))
    }

    fun addImage(imageUri: String, imageName: String?) = viewModelScope.launch {
        UserData.entity?.pictureUri?.add(imageUri)
        UserData.entity?.pictureFilenames?.add(imageName ?: "")
        _modelState.emit(_modelState.value.copy(
            imageUris = UserData.entity?.pictureUri?.toList() ?: emptyList(),
            imageFilenames = UserData.entity?.pictureFilenames?.toList() ?: emptyList(),
            lastUpdated = System.currentTimeMillis(),
        ))
    }

    fun removeImage(imageUri: String) = viewModelScope.launch {
        val idx = UserData.entity?.pictureUri?.indexOf(imageUri) ?: error("Invalid image index")
        UserData.entity?.pictureUri?.removeAt(idx)
        UserData.entity?.pictureFilenames?.removeAt(idx)
        _modelState.emit(_modelState.value.copy(
            imageUris = UserData.entity?.pictureUri?.toList() ?: emptyList(),
            imageFilenames = UserData.entity?.pictureFilenames?.toList() ?: emptyList(),
            lastUpdated = System.currentTimeMillis(),
        ))
    }


}
