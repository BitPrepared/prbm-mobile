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
  var title: String,
  var authors: String? = null,
  var place: String? = null,
  var note: String? = null,
  /** Date of Prbm (different from timestamp)  */
  var date: String? = null,
  var time: String? = null,
  val units: MutableList<PrbmUnit> = mutableListOf(PrbmUnit()),
  val coordinates: MutableList<PrbmCoordinates> = mutableListOf()
)
