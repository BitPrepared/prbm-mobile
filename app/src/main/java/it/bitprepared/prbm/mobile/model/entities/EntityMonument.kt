package it.bitprepared.prbm.mobile.model.entities

import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.EntityField
import it.bitprepared.prbm.mobile.model.EntityFieldType
import it.bitprepared.prbm.mobile.model.PrbmEntity

private val defaultExtraField = arrayOf(
    EntityField(
        "Denominazione",
        "Inserisci la denominazione del luogo storico.",
        1,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Comune e Provincia",
        "Inserisci il comune e la provicina del monumento/luogo.",
        2,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Proprietà attuali",
        "Inserisci una breve descrizione dello stato attuale del monumento/luogo.",
        3,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Monumento - Fatto costruire da",
        "Inserisci il nome di chi ha commissionato la costruzione del monumento.",
        4,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Monumento - Perchè lo fece costruire",
        "Inserisci il motivo che ha portato alla costruzione del monumento.",
        5,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Monumento - Materiali",
        "Inserisci quali sono i materiali utilizzati nella costruzione.",
        6,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Monumento - Opere d'arte",
        "Inserisci la descrizione di eventuali opere d'arte presenti nelle vicinanze.",
        7,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Monumento - Data Costruzione", "Data Costruzione", 8, EntityFieldType.DATE
    ), EntityField(
        "Luogo - Da cosa è scaturito",
        "Inserisci come mai questo luogo storico è importante.",
        9,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Luogo - Racconto dell'avvenimento",
        "Inserisci la descrizione dell'avvennimento legato a questo luogo storico.",
        10,
        EntityFieldType.LONG_TEXT
    ), EntityField(
        "Luogo - Uso attuale",
        "Inserisci la descrizione di quale utilizzo se ne fa oggi di questo luogo.",
        11,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Luogo - Stato di manutenzione",
        "Inserisci lo stato di conservazione/manutenzione in cui si trova adesso questo luogo.",
        12,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Luogo - Data Avvenimento", "Data Avvenimento", 13, EntityFieldType.DATE
    )
)


/**
 * Entity used to represent a monument or an interesting place
 */
class EntityMonument @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Monumento"
    override val typeDescription: String =
        "Utilizza questa classe per inserire informazioni su monumenti o luoghi di rilevanza storica."

    override val idIconColor: Int
        get() = R.color.monument_back
}
