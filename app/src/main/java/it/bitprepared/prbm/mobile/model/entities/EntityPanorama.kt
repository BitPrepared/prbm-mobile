package it.bitprepared.prbm.mobile.model.entities

import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.EntityField
import it.bitprepared.prbm.mobile.model.EntityFieldType
import it.bitprepared.prbm.mobile.model.PrbmEntity

private var id = 1
/** Array of Extra fields values  */
private val defaultExtraField = arrayOf(
    EntityField(
        "Località", "Inserire il nome della località dell'ambiente osservato.",
        id++, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Tipo Ambiente", "Inserire la tipologia di ambiente osservato.",
        id++, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Descrizione della flora", "Inserire una breve descrizione della flora circostante.",
        id++, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Impatto dell'uomo",
        "Descrivere quale è stato l'impatto dell'uomo sull'ambiente osservato.",
        id++,
        EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Cosa ti è piaciuto di più?",
        "Indica ciò che ti è piaciuto di più in questo ambiente naturale.",
        id++,
        EntityFieldType.LONG_TEXT
    ),
    EntityField(
        "Cosa ti è piaciuto di meno?",
        "Indica ciò che ti è piaciuto di meno in questo ambiente naturale.",
        id++,
        EntityFieldType.LONG_TEXT
    ),
    EntityField(
        "Senzazioni personali positive",
        "Indica se la visione di questo ambiente di ha suscitato senzazioni positive.",
        id++,
        EntityFieldType.LONG_TEXT
    ),
    EntityField(
        "Senzazioni personali nevative",
        "Indica se la visione di questo ambiente di ha suscitato senzazioni negative.",
        id++,
        EntityFieldType.LONG_TEXT
    )
)


/**
 * Entity used to represent an observation of a panorama
 */
class EntityPanorama @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Ambiente Naturale"
    override val typeDescription: String =
        "Utilizza questa classe per inserire informazioni sull'ambiente naturale circostante e sui panorami ammirati."

    override val idListImage: Int
        get() = R.drawable.background_panorama_list

    override val idButtonImage: Int
        get() = R.drawable.button_panorama

    override val idBackImage: Int
        get() = R.color.panorama_back
}
