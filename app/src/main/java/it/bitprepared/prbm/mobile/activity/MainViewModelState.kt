package it.bitprepared.prbm.mobile.activity

data class MainViewModelState(
    val isUploading: Boolean,
    val uploadProgress: Float,
    val errorMessage: String? = null
)