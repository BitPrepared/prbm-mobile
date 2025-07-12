package it.bitprepared.prbm.mobile.model

/**
 * [PrbmUnit] represent a single row of a [Prbm] that is composed by several [PrbmEntity]
 * on 4 columns.
 */
data class PrbmUnit(
    var azimuth: Double = 0.0,
    var meters: Double = 0.0,
    var minutes: Int = 0,
    var coordinates: PrbmCoordinates = PrbmCoordinates(),
    val farLeft: MutableList<PrbmEntity> = mutableListOf(),
    val farRight: MutableList<PrbmEntity> = mutableListOf(),
    val nearLeft: MutableList<PrbmEntity> = mutableListOf(),
    val nearRight: MutableList<PrbmEntity> = mutableListOf(),
) {

    fun setAzimuth(azimuth: String) {
        this.azimuth = azimuth.toDoubleOrNull() ?: 0.0
    }

    /**
     * Setter for Unit Minutes
     * @param minutes The new unit minutes
     */
    fun setMinutes(minutes: String) {
        this.minutes = minutes.toIntOrNull() ?: 0
    }

    /**
     * Setter for Unit Meters
     * @param meter The new unit meters
     */
    fun setMeters(meter: String) {
        this.meters = meter.toDoubleOrNull() ?: 0.0
    }

    fun setCoordinatesFrom(lastCoordinates: PrbmCoordinates) {
        this.coordinates.latitude = lastCoordinates.latitude
        this.coordinates.longitude = lastCoordinates.longitude
        this.coordinates.time = lastCoordinates.time
        this.coordinates.bearing = lastCoordinates.bearing
        this.coordinates.speed = lastCoordinates.speed
    }

    fun hasCoordinates(): Boolean =
        this.coordinates.latitude != 0.0 && this.coordinates.longitude != 0.0
}
