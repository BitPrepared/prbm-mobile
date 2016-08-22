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

package it.bitprepared.prbm.mobile.model.entities;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.EntityField;
import it.bitprepared.prbm.mobile.model.EntityFieldType;
import it.bitprepared.prbm.mobile.model.PrbmEntity;

/**
 * Entity used to represent a weather/forecast observation
 * @author Nicola Corti
 */
public class EntityWeather extends PrbmEntity {

    /** Base View IDs */
    private int ID_FIELD_BASE = 1000;

    private static final String type = "Meteo";
    private static final String description = "Utilizza questa classe per inserire informazioni sulle condizioni atmosferiche osservate.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Temperatura (C°): ", "Temperatura (C°): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Umidità (%): ", "Umidità (%): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Pressione (mBar): ", "Pressione (mBar): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Velocità del vento (Km/h): ", "Velocità del vento (Km/h): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Direzione del vento", "Inserire la direzione del vento (Nord, Sud, Est, Ovest).",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Precipitazioni", "Inserire una breve descrizione delle precipitazioni attuali.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Quantità di pioggia (mm): ", "Quantità di pioggia (mm): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Acidità dell'acqua (Ph): ", "Acidità dell'acqua (Ph): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Neve", "Inserire una breve descrizione della neve (intensità, tipo di fiocco, torbidità dell'acqua)",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Neve, acidità dell'acqua distillata (Ph): ", "Neve, acidità dell'acqua distillata (Ph): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Grandine", "Inserire una breve descrizione della grandine (intensità, tipo di fiocco, torbidità dell'acqua)",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Grandine, diametro (cm): ", "Grandine, diametro (cm): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Grandine, Acidità dell'acqua (Ph): ", "Grandine, Acidità dell'acqua (Ph): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Eventi particolari", "Inserire eventuali eventi particolari (trombe d'aria, tornadi, cicloni, tempeste)",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT),
        new EntityField("Sole, angolazione (°): ", "Sole, angolazione (°): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Sole, eventuali anomalie", "Inserire eventuali anomalie solari quali eclissi parziali o totali",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Nebbia, visibilità (m): ", "Nebbia, visibilità (m): ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Nebbia, tipo di formazione", "Inserire il tipo di formazione della nebbia (nebbia, nuvole, evaporazione)",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Nubi, velocità di spostamento: ", "Nubi, velocità di spostamento: ",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Nubi, tipologia", "Inserire la tipologia delle nubi osservate (nembi, cumoli, cirri, etc.)",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        };

    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityWeather(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityFauna
     */
    public EntityWeather() {
        this("", "", "");
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getTypeDescription() {
        return description;
    }

    @Override
    public EntityField[] getExtraFields() {
        return this.extraFields;
    }

    @Override
    public int getIdListImage() {
        return R.drawable.background_forecast_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_forecast;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_forecast;
    }

}
