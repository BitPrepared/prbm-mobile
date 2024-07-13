/*   This file is part of PrbmMobile
 *
 *   PrbmMobile is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   PrbmMobile is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with PrbmMobile.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    override val idListImage: Int
        get() = R.drawable.background_news_list

    override val idButtonImage: Int
        get() = R.drawable.button_news

    override val idBackImage: Int
        get() = R.color.news_back
}
