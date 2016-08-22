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
 * Entity used to represent a generic observation or a curiosity
 * @author Nicola Corti
 */
public class EntityCuriosity extends PrbmEntity {

    /** Base View IDs */
    private int ID_FIELD_BASE = 1000;

    private static final String type = "Curiosità";
    private static final String description = "Utilizza questa classe per inserire ulteriori curiosità oppure osservazioni che non rientrano nelle altre classi.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Perchè è importante?", "Come mai è importante riportare questa curiosità?",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT),
        new EntityField("Impressioni/Stato d'animo",
                        "Inserire una breve descrizione delle impressioni o dello stato d'animo.",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT)
    };

    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityCuriosity(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityFauna
     */
    public EntityCuriosity() {
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
        return R.drawable.background_curiosity_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_curiosity;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_curiosity;
    }
}
