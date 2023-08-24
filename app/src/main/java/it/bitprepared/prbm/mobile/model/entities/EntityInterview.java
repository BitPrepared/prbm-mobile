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
 * Entity used to represent an interview to a specific person
 */
public class EntityInterview extends PrbmEntity {

    /** Base View IDs */
    private int id = 1;

    private String type = "Intervista";
    private String
        typeDesc = "Utilizza questa classe per inserire report o interviste alle persone incontrate.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Età: ", "Eta",
                        id++, EntityFieldType.NUMERIC),
        new EntityField("Professione", "Inserire la professione dell'intervistato.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Stato civile", "Inserire lo stato civile dell'intervistato.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Ruolo", "Indicare in quale veste il soggetto è stato intervistato.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Scopo dell'intervista", "Indicare quale è il motivo/scopo di questa intervista.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Come si è arrivati a lui", "Indicare attraverso quali contatti si è arrivati al soggetto intervistato.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Stato d'animo", "Indicare lo stato d'animo dell'intervistato.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Gradimento", "Indicare il grado di gradimento dell'intervistato.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Affidabilità", "Indicare il grado di affidabilità dell'intervistato.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Rapporto con l'intervistato", "Indicare che genere di rapporto si è avuto con l'intervistato.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Trascrizione dell'intervista", "Riportare la trascrizione integrale dell'intervista.",
                        id++, EntityFieldType.LONG_TEXT),
    };


    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityInterview(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityFauna
     */
    public EntityInterview() {
        this("", "", "");
    }


    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityInterview(String description, String caption, String timestamp, EntityField[] extraFields) {
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
        return R.drawable.background_interview_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_interview;
    }

    @Override
    public int getIdBackImage() {
        return R.color.interview_back;
    }

}
