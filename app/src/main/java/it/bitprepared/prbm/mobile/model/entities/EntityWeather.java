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
 */
public class EntityWeather extends PrbmEntity {

    /** Base View IDs */
    private int id = 1;

    private String type = "Meteo";
    private String
        typeDesc = "Utilizza questa classe per inserire informazioni sulle condizioni atmosferiche osservate.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Temperatura (C°): ", "Temperatura (C°): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Umidità (%): ", "Umidità (%): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Pressione (mBar): ", "Pressione (mBar): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Velocità del vento (Km/h): ", "Velocità del vento (Km/h): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Direzione del vento", "Inserire la direzione del vento (Nord, Sud, Est, Ovest).",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Precipitazioni", "Inserire una breve descrizione delle precipitazioni attuali.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Quantità di pioggia (mm): ", "Quantità di pioggia (mm): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Acidità dell'acqua (Ph): ", "Acidità dell'acqua (Ph): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Neve", "Inserire una breve descrizione della neve (intensità, tipo di fiocco, torbidità dell'acqua)",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Neve, acidità dell'acqua distillata (Ph): ", "Neve, acidità dell'acqua distillata (Ph): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Grandine", "Inserire una breve descrizione della grandine (intensità, tipo di fiocco, torbidità dell'acqua)",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Grandine, diametro (cm): ", "Grandine, diametro (cm): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Grandine, Acidità dell'acqua (Ph): ", "Grandine, Acidità dell'acqua (Ph): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Eventi particolari", "Inserire eventuali eventi particolari (trombe d'aria, tornadi, cicloni, tempeste)",
                        id++, EntityFieldType.LONG_TEXT),
        new EntityField("Sole, angolazione (°): ", "Sole, angolazione (°): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Sole, eventuali anomalie", "Inserire eventuali anomalie solari quali eclissi parziali o totali",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Nebbia, visibilità (m): ", "Nebbia, visibilità (m): ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Nebbia, tipo di formazione", "Inserire il tipo di formazione della nebbia (nebbia, nuvole, evaporazione)",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Nubi, velocità di spostamento: ", "Nubi, velocità di spostamento: ",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Nubi, tipologia", "Inserire la tipologia delle nubi osservate (nembi, cumoli, cirri, etc.)",
                        id++, EntityFieldType.SHORT_TEXT),
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


    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityWeather(String description, String caption, String timestamp, EntityField[] extraFields) {
        this(description, caption, timestamp);
        this.extraFields = extraFields;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getTypeDescription() {
        return typeDesc;
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
        return R.color.ForecastBack;
    }

}
