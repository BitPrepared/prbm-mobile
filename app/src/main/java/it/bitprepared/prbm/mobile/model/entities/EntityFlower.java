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
 */
public class EntityFlower extends PrbmEntity {

    /** Base View IDs */
    private int id = 1;

    private String type = "Fiore/Erba";
    private String typeDesc = "Utilizza questa classe per inserire avvistamente di fiori o erba.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Nome comune", "Inserire il nome comune del fiore.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Nome scientifico", "Inserire il nome scientifico dal fiore.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Numero petali: ", "Numero petali",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Altezza media (m): ", "Altezza media",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Descrizione foglie", "Inserire una breve descrizione delle foglie del fiore.",
                        id++, EntityFieldType.LONG_TEXT),
        new EntityField("Habitat tipico", "Inserire la descrizione dell'habitat tipico della pianta.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Distribuzione", "Inserire la descrizione della distribuzione della pianta.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Proprietà", "Inserire eventuali proprietà di questa pianta.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Curiosità", "Spazio per eventuali curiosità.",
                        id++, EntityFieldType.SHORT_TEXT)
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

    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityFlower(String description, String caption, String timestamp, EntityField[] extraFields) {
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
        return R.drawable.background_flower_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_flower;
    }

    @Override
    public int getIdBackImage() {
        return R.color.FlowerBack;
    }
}
