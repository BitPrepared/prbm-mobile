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
 * Entity used to represent a building
 */
public class EntityBuilding extends PrbmEntity {

    /** Base View IDs */
    private int id = 1;

    private String type = "Edificio";
    private String typeDesc = "Utilizza questa classe per inserire informazioni su edifici o costruzioni.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Descrizione", "Inserisci una breve descrizione dell'edificio/struttura",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Materiale", "Di quali materiali è composta la struttura (mattoni, legno, pietra, etc...)",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Scopo", "Quale è lo scopo della struttura?",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Stato di conservazione", "In quale stato verte attualmente la struttura?",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Proprietà", "Indicare se si tratta di una proprietà privata o pubblica e l'eventuale proprietario",
                        id++, EntityFieldType.SHORT_TEXT)
        };

    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityBuilding(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityBuilding
     */
    public EntityBuilding() {
        this("", "", "");
    }


    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityBuilding(String description, String caption, String timestamp, EntityField[] extraFields) {
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
        return R.drawable.background_building_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_building;
    }

    @Override
    public int getIdBackImage() {
        return R.color.BuildingBack;
    }

}
