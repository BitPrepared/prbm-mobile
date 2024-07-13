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
        "Descrizione",
        "Inserisci una breve descrizione dell'edificio/struttura",
        1,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Materiale",
        "Di quali materiali è composta la struttura (mattoni, legno, pietra, etc...)",
        2,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Scopo", "Quale è lo scopo della struttura?", 3, EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Stato di conservazione",
        "In quale stato verte attualmente la struttura?",
        4,
        EntityFieldType.SHORT_TEXT
    ), EntityField(
        "Proprietà",
        "Indicare se si tratta di una proprietà privata o pubblica e l'eventuale proprietario",
        5,
        EntityFieldType.SHORT_TEXT
    )
)

/**
 * Entity used to represent a building
 */
class EntityBuilding @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Edificio"

    override val typeDescription: String =
        "Utilizza questa classe per inserire informazioni su edifici o costruzioni."

    override val idListImage: Int
        get() = R.drawable.background_building_list

    override val idButtonImage: Int
        get() = R.drawable.button_building

    override val idBackImage: Int
        get() = R.color.building_back
}
