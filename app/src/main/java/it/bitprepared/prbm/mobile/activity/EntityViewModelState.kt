package it.bitprepared.prbm.mobile.activity

data class EntityViewModelState(
    val time: String = "",
    val title: String = "",
    val description: String = "",
    val saveReady: Boolean = false,
)