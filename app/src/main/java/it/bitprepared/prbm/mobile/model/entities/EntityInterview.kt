package it.bitprepared.prbm.mobile.model.entities

import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.EntityField
import it.bitprepared.prbm.mobile.model.EntityFieldType
import it.bitprepared.prbm.mobile.model.PrbmEntity

private val defaultExtraField = arrayOf(
    EntityField(
        "Età: ", "Eta",
        1, EntityFieldType.NUMERIC
    ),
    EntityField(
        "Professione", "Inserire la professione dell'intervistato.",
        2, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Stato civile", "Inserire lo stato civile dell'intervistato.",
        3, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Ruolo", "Indicare in quale veste il soggetto è stato intervistato.",
        4, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Scopo dell'intervista", "Indicare quale è il motivo/scopo di questa intervista.",
        5, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Come si è arrivati a lui",
        "Indicare attraverso quali contatti si è arrivati al soggetto intervistato.",
        6,
        EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Stato d'animo", "Indicare lo stato d'animo dell'intervistato.",
        7, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Gradimento", "Indicare il grado di gradimento dell'intervistato.",
        8, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Affidabilità", "Indicare il grado di affidabilità dell'intervistato.",
        9, EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Rapporto con l'intervistato",
        "Indicare che genere di rapporto si è avuto con l'intervistato.",
        10,
        EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Trascrizione dell'intervista", "Riportare la trascrizione integrale dell'intervista.",
        11, EntityFieldType.LONG_TEXT
    ),
)


/**
 * Entity used to represent an interview to a specific person
 */
class EntityInterview @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Intervista"
    override val typeDescription: String =
        "Utilizza questa classe per inserire report o interviste alle persone incontrate."

    override val idListImage: Int
        get() = R.drawable.background_interview_list

    override val idButtonImage: Int
        get() = R.drawable.button_interview

    override val idBackImage: Int
        get() = R.color.interview_back
}
