package it.bitprepared.prbm.mobile.activity

data class CreateEditPrbmViewModelState(
    val date: String = "",
    val time: String = "",
    val title: String = "",
    val authors: String = "",
    val place: String = "",
    val notes: String = "",
    val errorMessageResId: Int? = null,
    val saveReady: Boolean = false,
    val isEditing: Boolean = false
)