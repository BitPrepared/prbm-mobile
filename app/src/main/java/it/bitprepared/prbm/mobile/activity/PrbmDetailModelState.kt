package it.bitprepared.prbm.mobile.activity

import it.bitprepared.prbm.mobile.model.Prbm

data class PrbmDetailModelState(
    val prbm: Prbm,
    val saveSuccessful: Boolean? = null,
    val editReady: Boolean? = null,
    val editUnitReady: Boolean? = null,
    val stateTimestamp: Long = System.currentTimeMillis()
)
