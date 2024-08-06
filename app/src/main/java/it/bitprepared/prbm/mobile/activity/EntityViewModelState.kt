package it.bitprepared.prbm.mobile.activity

data class EntityViewModelState(
    val typeDescription: String,
    val time: String = "",
    val title: String = "",
    val description: String = "",
    val saveReady: Boolean = false,
)