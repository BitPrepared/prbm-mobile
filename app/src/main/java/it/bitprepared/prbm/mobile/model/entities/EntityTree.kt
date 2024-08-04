package it.bitprepared.prbm.mobile.model.entities

import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.EntityField
import it.bitprepared.prbm.mobile.model.EntityFieldType
import it.bitprepared.prbm.mobile.model.PrbmEntity

private var id = 1
private val defaultExtraField = arrayOf(
    EntityField(
        "Nome comune", "Inserire il nome comune della pianta.", id++, EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Nome scientifico",
        "Inserire il nome scientifico della pianta.",
        id++,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Altezza media (m): ", "Altezza Media", id++, EntityFieldType.NUMERIC
    ), EntityField(
        "Portamento",
        "Inserire il portamento della pianta (espanso, conico, fastigliato, colonnare, ombrelliforme, ricadente, scadente, prostrato, strisciante, etc.) ",
        id++,
        EntityFieldType.LONG_TEXT
    ), EntityField(
        "Corteccia",
        "Inserire informazioni sulla corteccia della pianta.",
        id++,
        EntityFieldType.LONG_TEXT
    ), EntityField(
        "Foglie",
        "Inserire informazioni sulle foglie della pianta (forma, nervature, margine, lamina, picciolo, colore, peli e superficie).",
        id++,
        EntityFieldType.LONG_TEXT
    ), EntityField(
        "Fiori e frutti",
        "Inserire informazioni sui fiori e sui frutti della pianta.",
        id++,
        EntityFieldType.LONG_TEXT
    ), EntityField(
        "Habitat tipico",
        "Inserire la descrizione dell'habitat tipico della pianta.",
        id++,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Habitat tipico",
        "Inserire la descrizione dell'habitat tipico della pianta.",
        id++,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Proprietà",
        "Inserire eventuali proprietà di questa pianta.",
        id++,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Proprietà",
        "Inserire eventuali proprietà di questa pianta.",
        id++,
        EntityFieldType.SHORT_TEXT
    )
)


/**
 * Entity used to represent an observation of a tree or similar
 */
class EntityTree @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Albero/Arbusto"
    override val typeDescription: String =
        "Utilizza questa classe per inserire avvistamente di alberi, foreste, arbosti o altro tipo di vegetazione"

    override val idListImage: Int
        get() = R.drawable.background_tree_list

    override val idButtonImage: Int
        get() = R.drawable.button_tree

    override val idIconColor: Int
        get() = R.color.tree_back
}
