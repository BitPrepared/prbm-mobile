package it.bitprepared.prbm.mobile.activity

import androidx.annotation.StringRes
import it.bitprepared.prbm.mobile.model.Prbm

enum class GpsStatus {
    DISABLED,
    PAIRING,
    FIXED,
}

data class PrbmDetailModelState(
    val prbm: Prbm,
    val editReady: Boolean? = null,
    val editUnitReady: Boolean? = null,
    val stateTimestamp: Long = System.currentTimeMillis(),
    @StringRes val errorMessage: Int? = null,
    val gpsStatus: GpsStatus = GpsStatus.DISABLED
)
