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
 * Entity used to represent an observation of a panorama
 * @author Nicola Corti
 */
public class EntityPanorama extends PrbmEntity {

    /** Base View IDs */
    private int ID_FIELD_BASE = 1000;

    private static final String type = "Ambiente Naturale";
    private static final String description = "Utilizza questa classe per inserire informazioni sull'ambiente naturale circostante e sui panorami ammirati.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Località", "Inserire il nome della località dell'ambiente osservato.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Tipo Ambiente", "Inserire la tipologia di ambiente osservato.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Descrizione della flora", "Inserire una breve descrizione della flora circostante.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Impatto dell'uomo", "Descrivere quale è stato l'impatto dell'uomo sull'ambiente osservato.",
                        ID_FIELD_BASE++, EntityFieldType.SHORT_TEXT),
        new EntityField("Cosa ti è piaciuto di più?", "Indica ciò che ti è piaciuto di più in questo ambiente naturale.",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT),
        new EntityField("Cosa ti è piaciuto di meno?", "Indica ciò che ti è piaciuto di meno in questo ambiente naturale.",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT),
        new EntityField("Senzazioni personali positive", "Indica se la visione di questo ambiente di ha suscitato senzazioni positive.",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT),
        new EntityField("Senzazioni personali nevative", "Indica se la visione di questo ambiente di ha suscitato senzazioni negative.",
                        ID_FIELD_BASE++, EntityFieldType.LONG_TEXT)
    };

    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityPanorama(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityFauna
     */
    public EntityPanorama() {
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
        return R.drawable.background_panorama_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_panorama;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_panorama;
    }
}
