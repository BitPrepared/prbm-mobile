package it.bitprepared.prbm.mobile.model

import java.util.Collections

/**
 * [PrbmUnit] represent a single row of a [Prbm] that is composed by several [PrbmEntity]
 * on 4 columns.
 */
data class PrbmUnit(
    /** Azimuth angle of this PrbmUnit  */
    var azimuth: Int = 0,
    /** Meters of this PrbmUnit  */
    var meter: Int = 0,
    /** Minutes elapsed of this PrbmUnit  */
    var minutes: Int = 0,
    /** List of Entities far Left  */
    private val entitiesFarLeft: MutableList<PrbmEntity> = mutableListOf(),
    /** List of Entities far Right  */
    private val entitiesFarRight: MutableList<PrbmEntity> = mutableListOf(),
    /** List of Entities near Left  */
    private val entitiesNearLeft: MutableList<PrbmEntity> = mutableListOf(),
    /** List of Entities near Left  */
    private val entitiesNearRight: MutableList<PrbmEntity> = mutableListOf(),
) {

    @JvmField
    var longitude: Double = 0.0

    @JvmField
    var latitude: Double = 0.0

    @Transient
    var isFlagAcquiringGPS: Boolean = false

    fun setAzimuth(azimuth: String) {
        this.azimuth = azimuth.toIntOrNull() ?: 0
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
        this.meter = meter.toIntOrNull() ?: 0
    }

    val farLeft: List<PrbmEntity>
        get() = entitiesFarLeft

    val farRight: List<PrbmEntity>
        get() = entitiesFarRight

    val nearLeft: List<PrbmEntity>
        get() = entitiesNearLeft

    val nearRight: List<PrbmEntity>
        get() = entitiesNearRight

    /** Add an entity in a specified column */
    fun addEntity(entity: PrbmEntity, column: Int) = when (column) {
        0 -> entitiesFarLeft
        1 -> entitiesNearLeft
        2 -> entitiesNearRight
        3 -> entitiesFarRight
        else -> error("Column must be between 0 and 3")
    }.add(entity)

    /** Getter for entity list from a specified column */
    fun getEntitiesFromColumn(column: Int): List<PrbmEntity> = when (column) {
        0 -> entitiesFarLeft
        1 -> entitiesNearLeft
        2 -> entitiesNearRight
        3 -> entitiesFarRight
        else -> error("Column must be between 0 and 3")
    }

    /**
     * Delete an entity from a specified column
     */
    fun deleteEntity(entity: PrbmEntity, column: Int) {
        when (column) {
            0 -> entitiesFarLeft
            1 -> entitiesNearLeft
            2 -> entitiesNearRight
            3 -> entitiesFarRight
            else -> error("Column must be between 0 and 3")
        }.remove(entity)
    }

    /**
     * Move an entity within a specified column
     * @param entity Entity to add
     * @param column Involved column (0-3)
     * @param down   Boolean flag to specify if movement must be downside or upside
     */
    fun moveEntity(entity: PrbmEntity, column: Int, down: Boolean) {
        val toMove = when (column) {
            0 -> entitiesFarLeft
            1 -> entitiesNearLeft
            2 -> entitiesNearRight
            3 -> entitiesFarRight
            else -> error("Column must be between 0 and 3")
        }
        val index = toMove.indexOf(entity)
        if (down) {
            Collections.swap(toMove, index, index + 1)
        } else {
            Collections.swap(toMove, index, index - 1)
        }
    }
}