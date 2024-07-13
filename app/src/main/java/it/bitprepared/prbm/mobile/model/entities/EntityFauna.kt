package it.bitprepared.prbm.mobile.model.entities

import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.EntityField
import it.bitprepared.prbm.mobile.model.EntityFieldType
import it.bitprepared.prbm.mobile.model.PrbmEntity

private val defaultExtraField = arrayOf(
    EntityField(
        "Nome comune", "Inserire il nome comune dell'animale.", 1, EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Nome scientifico",
        "Inserire il nome scientifico dall'animale.",
        2,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Dimensione", "Inserire la dimensione tipica dell'animale.", 3, EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Corteggiamento e riproduzione",
        "Inserire dettagli sulle abitudini di corteggiamento e riproduzione della specie.",
        4,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Tracce/Segni di presenza",
        "Inserire eventuali ritrovamenti di tracce o di segni di presenza dell'animale.",
        5,
        EntityFieldType.LONG_TEXT
    ), EntityField(
        "Escrementi",
        "Inserire dettagli sugli escrementi dell'animale.",
        6,
        EntityFieldType.LONG_TEXT
    ), EntityField(
        "I piccoli e la prole",
        "Inserire dettagli sull'accudimento della prole da parte dell'animale.",
        7,
        EntityFieldType.LONG_TEXT
    ), EntityField(
        "Alimentazione",
        "Informazioni sulle abitudini alimentari dell'animale.",
        8,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Posizione nella catena alimentare",
        "Inserire la posizione che l'animale ricopre all'interno della catena alimentare.",
        9,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Tane/Nidi/Rifugi",
        "Inserire eventuali dettagli su avvistamenti di tane, nidi o rifugi dell'animale.",
        10,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Attacco/Difesa/Mimetismo",
        "Inserire dettagli sulle tecniche di attacco, difesa o mimetismo utilizzate dall'animale.",
        11,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Habitat tipico",
        "Inserire la descrizione dell'habitat tipico dell'animale.",
        12,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Distribuzione",
        "Inserire la descrizione della distribuzione dell'animale.",
        13,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Territorialità",
        "Inserire eventuali informazioni sulla territorialità della specie.",
        14,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Curiosità", "Spazio per eventuali curiosità.", 15, EntityFieldType.SHORT_TEXT
    )
)

/**
 * Entity used to represent an animal observation
 */
class EntityFauna @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Fauna"
    override val typeDescription: String =
        "Utilizza questa classe per inserire avvistamente di animali, nidi, insetti o altri avvistamenti relativi alla fauna."

    override val idListImage: Int
        get() = R.drawable.background_fauna_list

    override val idButtonImage: Int
        get() = R.drawable.button_fauna

    override val idBackImage: Int
        get() = R.color.fauna_back
}
