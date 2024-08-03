package it.bitprepared.prbm.mobile.activity

import it.bitprepared.prbm.mobile.model.PrbmUnit

sealed class PrbmUnitAdapterViewType(val idx: Int) {
    data object AddButton : PrbmUnitAdapterViewType(0)
    data class Unit(val prbmUnit: PrbmUnit) : PrbmUnitAdapterViewType(1) {
        companion object {
            const val idx = 1
        }
    }
}