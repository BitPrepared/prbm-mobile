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
 * Entity used to represent a news or a report
 * @author Nicola Corti
 */
public class EntityNews extends PrbmEntity {

    /** Base View IDs */
    private int ID_FIELD_BASE = 1000;

    private static final String type = "Fatto di Cronaca";
    private static final String description = "Utilizza questa classe per riportare notizie o fatti di cronaca del posto.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Luogo", "Inserire il luogo dove si è svolto il fatto di cronaca.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Persone coinvolte", "Inserire l'elenco delle persone coinvolte.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Cosa è successo?", "Inserire la descrizione del fatto di cronaca.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Perchè?", "Inserire le motivazioni o ciò che ha provocato il fatto.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Se negativo, poteva essere evitato?", "Inserire se, ed in quali circostanze, il fatto di cronaca negativo poteva essere evitato.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Conseguenze", "Inserire le conseguenze del fatto di cronaca.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Cosa hai imparato?", "Inserire cosa hai imparato da questo fatto di cronaca.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT)
    };


    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityNews(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityNews
     */
    public EntityNews() {
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
        return R.drawable.background_news_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_news;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_news;
    }
}
