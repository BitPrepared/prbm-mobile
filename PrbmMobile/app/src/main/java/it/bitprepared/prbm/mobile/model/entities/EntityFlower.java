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
 * Entity used to represent a flower/grass observation
 * @author Nicola Corti
 */
public class EntityFlower extends PrbmEntity {

    /** Base View IDs */
    private int ID_FIELD_BASE = 1000;

    private static final String type = "Fiore/Erba";
    private static final String description = "Utilizza questa classe per inserire avvistamente di fiori o erba.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Nome comune", "Inserire il nome comune del fiore.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Nome scientifico", "Inserire il nome scientifico dal fiore.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Numero petali: ", "Numero petali",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Altezza media (m): ", "Altezza media",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Descrizione foglie", "Inserire una breve descrizione delle foglie del fiore.",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT),
        new EntityField("Habitat tipico", "Inserire la descrizione dell'habitat tipico della pianta.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Distribuzione", "Inserire la descrizione della distribuzione della pianta.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Proprietà", "Inserire eventuali proprietà di questa pianta.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Curiosità", "Spazio per eventuali curiosità.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT)
        };

    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityFlower(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityFlower
     */
    public EntityFlower() {
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
        return R.drawable.background_flower_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_flower;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_flower;
    }
}
