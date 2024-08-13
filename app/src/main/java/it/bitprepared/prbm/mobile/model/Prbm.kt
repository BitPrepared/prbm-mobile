package it.bitprepared.prbm.mobile.model

import androidx.multidex.BuildConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

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
  val uuid: UUID = UUID.randomUUID(),
  val timestamp: String = getCurrentTimeInIsoFormat(),
  val title: String,
  val authors: String? = null,
  val place: String? = null,
  val note: String? = null,
  /** Date of Prbm (different from timestamp)  */
  val date: String? = null,
  val time: String? = null,
  val units: MutableList<PrbmUnit> = mutableListOf(PrbmUnit()),
  val coordinates: MutableList<PrbmCoordinates> = mutableListOf()
)
