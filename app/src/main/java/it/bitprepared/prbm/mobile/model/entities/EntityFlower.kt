package it.bitprepared.prbm.mobile.model.entities

import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.EntityField
import it.bitprepared.prbm.mobile.model.EntityFieldType
import it.bitprepared.prbm.mobile.model.PrbmEntity

private val defaultExtraField = arrayOf(
    EntityField(
        "Nome comune", "Inserire il nome comune del fiore.", 1, EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Nome scientifico", "Inserire il nome scientifico dal fiore.", 2, EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Numero petali: ", "Numero petali", 3, EntityFieldType.NUMERIC
    ), EntityField(
        "Altezza media (m): ", "Altezza media", 4, EntityFieldType.NUMERIC
    ), EntityField(
        "Descrizione foglie",
        "Inserire una breve descrizione delle foglie del fiore.",
        5,
        EntityFieldType.LONG_TEXT
    ), EntityField(
        "Habitat tipico",
        "Inserire la descrizione dell'habitat tipico della pianta.",
        6,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Distribuzione",
        "Inserire la descrizione della distribuzione della pianta.",
        7,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Proprietà", "Inserire eventuali proprietà di questa pianta.", 8, EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Curiosità", "Spazio per eventuali curiosità.", 9, EntityFieldType.SHORT_TEXT
    )
)

/**
 * Entity used to represent a flower/grass observation
 */
class EntityFlower @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Fiore/Erba"
    override val typeDescription: String =
        "Utilizza questa classe per inserire avvistamente di fiori o erba."

    override val idIconColor: Int
        get() = R.color.flower_black
}
