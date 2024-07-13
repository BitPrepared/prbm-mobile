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

private var id = 1
private val defaultExtraField = arrayOf(
    EntityField(
        "Temperatura (C°): ", "Temperatura (C°): ", id++, EntityFieldType.NUMERIC
    ),
    EntityField(
        "Umidità (%): ", "Umidità (%): ", id++, EntityFieldType.NUMERIC
    ),
    EntityField(
        "Pressione (mBar): ", "Pressione (mBar): ", id++, EntityFieldType.NUMERIC
    ),
    EntityField(
        "Velocità del vento (Km/h): ", "Velocità del vento (Km/h): ", id++, EntityFieldType.NUMERIC
    ),
    EntityField(
        "Direzione del vento",
        "Inserire la direzione del vento (Nord, Sud, Est, Ovest).",
        id++,
        EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Precipitazioni",
        "Inserire una breve descrizione delle precipitazioni attuali.",
        id++,
        EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Quantità di pioggia (mm): ", "Quantità di pioggia (mm): ", id++, EntityFieldType.NUMERIC
    ),
    EntityField(
        "Acidità dell'acqua (Ph): ", "Acidità dell'acqua (Ph): ", id++, EntityFieldType.NUMERIC
    ),
    EntityField(
        "Neve",
        "Inserire una breve descrizione della neve (intensità, tipo di fiocco, torbidità dell'acqua)",
        id++,
        EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Neve, acidità dell'acqua distillata (Ph): ",
        "Neve, acidità dell'acqua distillata (Ph): ",
        id++,
        EntityFieldType.NUMERIC
    ),
    EntityField(
        "Grandine",
        "Inserire una breve descrizione della grandine (intensità, tipo di fiocco, torbidità dell'acqua)",
        id++,
        EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Grandine, diametro (cm): ", "Grandine, diametro (cm): ", id++, EntityFieldType.NUMERIC
    ),
    EntityField(
        "Grandine, Acidità dell'acqua (Ph): ",
        "Grandine, Acidità dell'acqua (Ph): ",
        id++,
        EntityFieldType.NUMERIC
    ),
    EntityField(
        "Eventi particolari",
        "Inserire eventuali eventi particolari (trombe d'aria, tornadi, cicloni, tempeste)",
        id++,
        EntityFieldType.LONG_TEXT
    ),
    EntityField(
        "Sole, angolazione (°): ", "Sole, angolazione (°): ", id++, EntityFieldType.NUMERIC
    ),
    EntityField(
        "Sole, eventuali anomalie",
        "Inserire eventuali anomalie solari quali eclissi parziali o totali",
        id++,
        EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Nebbia, visibilità (m): ", "Nebbia, visibilità (m): ", id++, EntityFieldType.NUMERIC
    ),
    EntityField(
        "Nebbia, tipo di formazione",
        "Inserire il tipo di formazione della nebbia (nebbia, nuvole, evaporazione)",
        id++,
        EntityFieldType.SHORT_TEXT
    ),
    EntityField(
        "Nubi, velocità di spostamento: ",
        "Nubi, velocità di spostamento: ",
        id++,
        EntityFieldType.NUMERIC
    ),
    EntityField(
        "Nubi, tipologia",
        "Inserire la tipologia delle nubi osservate (nembi, cumoli, cirri, etc.)",
        id++,
        EntityFieldType.SHORT_TEXT
    ),
)

/**
 * Entity used to represent a weather/forecast observation
 */
class EntityWeather @JvmOverloads constructor(
    description: String = "",
    caption: String = "",
    timestamp: String = "",
    override val extraFields: Array<EntityField> = defaultExtraField
) : PrbmEntity(description, caption, timestamp) {

    override val type: String = "Meteo"
    override val typeDescription: String =
        "Utilizza questa classe per inserire informazioni sulle condizioni atmosferiche osservate."

    override val idListImage: Int
        get() = R.drawable.background_forecast_list

    override val idButtonImage: Int
        get() = R.drawable.button_forecast

    override val idBackImage: Int
        get() = R.color.forecast_back
}
