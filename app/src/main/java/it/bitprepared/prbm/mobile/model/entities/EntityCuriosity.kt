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

    override val idListImage: Int
        get() = R.drawable.background_curiosity_list

    override val idButtonImage: Int
        get() = R.drawable.button_curiosity

    override val idBackImage: Int
        get() = R.color.curiosity_back
}
