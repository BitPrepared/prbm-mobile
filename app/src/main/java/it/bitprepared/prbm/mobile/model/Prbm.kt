package it.bitprepared.prbm.mobile.model

import android.util.Log
import androidx.multidex.BuildConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun getCurrentTimeInIsoFormat(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(Date())
}

/**
 * It represent a single Prbm, stored locally and composed by several [PrbmUnit] rows.
 */
data class Prbm(
    private val version: String = BuildConfig.VERSION_NAME,
    val timestamp: String = getCurrentTimeInIsoFormat(),
    val title: String,
    val authors: String? = null,
    val place: String? = null,
    val note: String? = null,
    /** Date of Prbm (different from timestamp)  */
    val date: String? = null,
    val units: MutableList<PrbmUnit> = mutableListOf(PrbmUnit())
) {

    fun getUnit(pos: Int): PrbmUnit = units[pos]

    /**
     * Public method to add a new empty unit at a specific position.
     * Units will be shifted subsequently.
     * @param posit Position of new Unit.
     * @param after Boolean flag to set if new unit must be inserted before or after posit
     */
    fun addNewUnits(posit: Int, after: Boolean) {
        if (after) {
            units.add(posit + 1, PrbmUnit())
        } else {
            units.add(posit, PrbmUnit())
        }
    }

    /**
     * Public method to add a new unit after a specific unit.
     * Units will be shifted subsequently.
     * @param unit Unit involved in adding.
     * @param after Boolean flag to set if new unit must be inserted before or after posit
     */
    fun addNewUnits(unit: PrbmUnit, after: Boolean) {
        // TODO Remove me
        val posit = units.indexOf(unit)
        addNewUnits(posit, after)
    }

    /**
     * Public method to check if Unit can be deleted. At least one unit must be present
     * @return True if Unit can be deleted, false otherwise.
     */
    fun canDelete(): Boolean = units.size > 1

    fun deleteUnit(u: PrbmUnit) = units.remove(u)

    fun deleteUnit(pos: Int) = units.removeAt(pos)

    fun debug() {
        Log.d(TAG, "--- Printing PRBM ---")
        for (i in units.indices) {
            val u = units[i]
            Log.d(TAG, " --- Unit $i")
            for (j in u.farLeft.indices) {
                val e = u.farLeft[j]
                Log.d(TAG, " ------ Entity " + j + " Tyep " + e.type)
            }
            for (j in u.nearLeft.indices) {
                val e = u.nearLeft[j]
                Log.d(TAG, " ------ Entity " + j + " Tyep " + e.type)
            }
            for (j in u.nearRight.indices) {
                val e = u.nearRight[j]
                Log.d(TAG, " ------ Entity " + j + " Tyep " + e.type)
            }
            for (j in u.farRight.indices) {
                val e = u.farRight[j]
                Log.d(TAG, " ------ Entity " + j + " Tyep " + e.type)
            }
        }
    }

    companion object {
        /** Debug TAG  */
        private const val TAG = "Prbm"
    }
}