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
 * Entity used to represent an animal observation
 * @author Nicola Corti
 */
public class EntityFauna extends PrbmEntity {

    /** Base View IDs */
    private int id = 1;

    private String type = "Fauna";
    private String typeDesc = "Utilizza questa classe per inserire avvistamente di animali, nidi, insetti o altri avvistamenti relativi alla fauna.";

    /** Array of Extra fields values */
    private EntityField[] extraFields = {
        new EntityField("Nome comune", "Inserire il nome comune dell'animale.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Nome scientifico", "Inserire il nome scientifico dall'animale.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Dimensione", "Inserire la dimensione tipica dell'animale.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Corteggiamento e riproduzione", "Inserire dettagli sulle abitudini di corteggiamento e riproduzione della specie.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Tracce/Segni di presenza", "Inserire eventuali ritrovamenti di tracce o di segni di presenza dell'animale.",
                        id++, EntityFieldType.LONG_TEXT),
        new EntityField("Escrementi", "Inserire dettagli sugli escrementi dell'animale.",
                        id++, EntityFieldType.LONG_TEXT),
        new EntityField("I piccoli e la prole", "Inserire dettagli sull'accudimento della prole da parte dell'animale.",
                        id++, EntityFieldType.LONG_TEXT),
        new EntityField("Alimentazione", "Informazioni sulle abitudini alimentari dell'animale.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Posizione nella catena alimentare", "Inserire la posizione che l'animale ricopre all'interno della catena alimentare.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Tane/Nidi/Rifugi", "Inserire eventuali dettagli su avvistamenti di tane, nidi o rifugi dell'animale.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Attacco/Difesa/Mimetismo", "Inserire dettagli sulle tecniche di attacco, difesa o mimetismo utilizzate dall'animale.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Habitat tipico", "Inserire la descrizione dell'habitat tipico dell'animale.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Distribuzione", "Inserire la descrizione della distribuzione dell'animale.",
                        id++, EntityFieldType.SHORT_TEXT),
        new EntityField("Territorialità", "Inserire eventuali informazioni sulla territorialità della specie.",
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
    public EntityFauna(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }


    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityFauna(String description, String caption, String timestamp, EntityField[] extraFields) {
        this(description, caption, timestamp);
        this.extraFields = extraFields;
    }

    /**
     * Base constructor for an empty EntityFauna
     */
    public EntityFauna() {
        this("", "", "");
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
        return R.drawable.background_fauna_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_fauna;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_fauna;
    }
}
