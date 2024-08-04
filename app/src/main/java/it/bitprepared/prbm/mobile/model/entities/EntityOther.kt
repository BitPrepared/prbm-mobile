package it.bitprepared.prbm.mobile.model.entities

import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.EntityField
import it.bitprepared.prbm.mobile.model.EntityFieldType
import it.bitprepared.prbm.mobile.model.PrbmEntity

private val defaultExtraField = arrayOf(
    EntityField(
        "Descrizione", "Inserisci la descrizione dell'osservazione", 1, EntityFieldType.LONG_TEXT
    ),
)

/**
 * Entity used to represent a building
 */
class EntityOther @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Altro"
    override val typeDescription: String =
        "Utilizza questa classe per inserire informazioni che non rientrano nella classi precedenti."

    override val idIconColor: Int
        get() = R.color.other_back
}
