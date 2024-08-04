package it.bitprepared.prbm.mobile.model.entities

import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.EntityField
import it.bitprepared.prbm.mobile.model.EntityFieldType
import it.bitprepared.prbm.mobile.model.PrbmEntity

private val defaultExtraField = arrayOf(
    EntityField(
        "Perchè è importante?", "Come mai è importante riportare questa curiosità?",
        1, EntityFieldType.LONG_TEXT
    ),
    EntityField(
        "Impressioni/Stato d'animo",
        "Inserire una breve descrizione delle impressioni o dello stato d'animo.",
        2, EntityFieldType.LONG_TEXT
    )
)

/**
 * Entity used to represent a generic observation or a curiosity
 */
class EntityCuriosity @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Curiosità"
    override val typeDescription: String =
        "Utilizza questa classe per inserire ulteriori curiosità oppure osservazioni che non rientrano nelle altre classi."

    override val idIconColor: Int
        get() = R.color.curiosity_back
}
