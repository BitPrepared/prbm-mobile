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
 * Entity used to represent a monument or an interesting place
 * @author Nicola Corti
 */
public class EntityMonument extends PrbmEntity {

    /** Base View IDs */
    private int id = 1;

    private String type = "Monumento";
    private String
        typeDesc = "Utilizza questa classe per inserire informazioni su monumenti o luoghi di rilevanza storica.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Denominazione", "Inserisci la denominazione del luogo storico.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Comune e Provincia", "Inserisci il comune e la provicina del monumento/luogo.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Proprietà attuali", "Inserisci una breve descrizione dello stato attuale del monumento/luogo.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Monumento - Fatto costruire da", "Inserisci il nome di chi ha commissionato la costruzione del monumento.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Monumento - Perchè lo fece costruire", "Inserisci il motivo che ha portato alla costruzione del monumento.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Monumento - Materiali", "Inserisci quali sono i materiali utilizzati nella costruzione.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Monumento - Opere d'arte", "Inserisci la descrizione di eventuali opere d'arte presenti nelle vicinanze.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Monumento - Data Costruzione", "Data Costruzione",
                        id++, EntityFieldType.DATE),
        new EntityField("Luogo - Da cosa è scaturito", "Inserisci come mai questo luogo storico è importante.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Luogo - Racconto dell'avvenimento", "Inserisci la descrizione dell'avvennimento legato a questo luogo storico.",
                        id++, EntityFieldType.LONG_TEXT),
        new EntityField("Luogo - Uso attuale", "Inserisci la descrizione di quale utilizzo se ne fa oggi di questo luogo.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Luogo - Stato di manutenzione", "Inserisci lo stato di conservazione/manutenzione in cui si trova adesso questo luogo.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Luogo - Data Avvenimento", "Data Avvenimento",
                        id++, EntityFieldType.DATE)
        };



    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityMonument(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityFauna
     */
    public EntityMonument() {
        this("", "", "");
    }


    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityMonument(String description, String caption, String timestamp, EntityField[] extraFields) {
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
        return R.drawable.background_monument_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_monument;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_monument;
    }

}
