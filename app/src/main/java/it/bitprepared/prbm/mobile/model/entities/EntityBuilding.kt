package it.bitprepared.prbm.mobile.model.entities

import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.EntityField
import it.bitprepared.prbm.mobile.model.EntityFieldType
import it.bitprepared.prbm.mobile.model.PrbmEntity

private val defaultExtraField = arrayOf(
    EntityField(
        "Descrizione",
        "Inserisci una breve descrizione dell'edificio/struttura",
        1,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Materiale",
        "Di quali materiali è composta la struttura (mattoni, legno, pietra, etc...)",
        2,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Scopo", "Quale è lo scopo della struttura?", 3, EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Stato di conservazione",
        "In quale stato verte attualmente la struttura?",
        4,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Proprietà",
        "Indicare se si tratta di una proprietà privata o pubblica e l'eventuale proprietario",
        5,
        EntityFieldType.SHORT_TEXT
    )
)

/**
 * Entity used to represent a building
 */
class EntityBuilding @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Edificio"

    override val typeDescription: String =
        "Utilizza questa classe per inserire informazioni su edifici o costruzioni."

    override val idIconColor: Int
        get() = R.color.building_back
}
