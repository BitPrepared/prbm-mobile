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

    override val idListImage: Int
        get() = R.drawable.background_flower_list

    override val idButtonImage: Int
        get() = R.drawable.button_flower

    override val idBackImage: Int
        get() = R.color.flower_black
}
