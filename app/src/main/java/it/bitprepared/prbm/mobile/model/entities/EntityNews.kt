package it.bitprepared.prbm.mobile.model.entities

import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.EntityField
import it.bitprepared.prbm.mobile.model.EntityFieldType
import it.bitprepared.prbm.mobile.model.PrbmEntity

private val defaultExtraField = arrayOf(
    EntityField(
        "Luogo", "Inserire il luogo dove si è svolto il fatto di cronaca.",
        1, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Persone coinvolte", "Inserire l'elenco delle persone coinvolte.",
        2, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Cosa è successo?", "Inserire la descrizione del fatto di cronaca.",
        3, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Perchè?", "Inserire le motivazioni o ciò che ha provocato il fatto.",
        4, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Se negativo, poteva essere evitato?",
        "Inserire se, ed in quali circostanze, il fatto di cronaca negativo poteva essere evitato.",
        5,
        EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Conseguenze", "Inserire le conseguenze del fatto di cronaca.",
        6, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Cosa hai imparato?", "Inserire cosa hai imparato da questo fatto di cronaca.",
        7, EntityFieldType.SHORT_TEXT
    )
)

/**
 * Entity used to represent a news or a report
 */
class EntityNews @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Fatto di Cronaca"
    override val typeDescription: String =
        "Utilizza questa classe per riportare notizie o fatti di cronaca del posto."

    override val idIconColor: Int
        get() = R.color.news_back
}
