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
 * Entity used to represent an observation of a tree or similar
 * @author Nicola Corti
 */
public class EntityTree extends PrbmEntity {

    /** Base View IDs */
    private int ID_FIELD_BASE = 1000;

    private static final String type = "Albero/Arbusto";
    private static final String description = "Utilizza questa classe per inserire avvistamente di alberi, foreste, arbosti o altro tipo di vegetazione";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Nome comune", "Inserire il nome comune della pianta.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Nome scientifico", "Inserire il nome scientifico della pianta.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Altezza media (m): ", "Altezza Media",
                        ID_FIELD_BASE++, EntityFieldType.NUMERIC),
        new EntityField("Portamento", "Inserire il portamento della pianta (espanso, conico, fastigliato, colonnare, ombrelliforme, ricadente, scadente, prostrato, strisciante, etc.) ",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT),
        new EntityField("Corteccia", "Inserire informazioni sulla corteccia della pianta.",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT),
        new EntityField("Foglie", "Inserire informazioni sulle foglie della pianta (forma, nervature, margine, lamina, picciolo, colore, peli e superficie).",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT),
        new EntityField("Fiori e frutti", "Inserire informazioni sui fiori e sui frutti della pianta.",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT),
        new EntityField("Habitat tipico", "Inserire la descrizione dell'habitat tipico della pianta.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Habitat tipico", "Inserire la descrizione dell'habitat tipico della pianta.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Proprietà", "Inserire eventuali proprietà di questa pianta.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Proprietà", "Inserire eventuali proprietà di questa pianta.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT)
    };

    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityTree(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityTree
     */
    public EntityTree() {
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
        return R.drawable.background_tree_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_tree;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_tree;
    }
}
