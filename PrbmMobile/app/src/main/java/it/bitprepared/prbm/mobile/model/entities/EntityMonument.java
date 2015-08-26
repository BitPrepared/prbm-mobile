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

import android.content.Context;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.activity.EntityViewHelper;
import it.bitprepared.prbm.mobile.model.PrbmEntity;

/**
 * Entity used to represent a monument or an interesting place
 * @author Nicola Corti
 */
public class EntityMonument extends PrbmEntity {

    /** List of View IDs */
    private static final int[] id_fields = {1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008,
            1009, 1010, 1011, 1012, 1013, 1014};
    /** List of Extra fields values */
    private List<String> extraFields = new ArrayList<>();


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


    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("entity-type", "monument");
            jsonObject.put("description", super.getDescription());
            jsonObject.put("caption", super.getCaption());
            jsonObject.put("minutes", super.getTimestamp());

            for(int i = 0; i < extraFields.size(); i++)
                jsonObject.put("monument-" + i,extraFields);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String getType() {
        return "Monumento";
    }

    @Override
    public String getTypeDescription() {
        return "Utilizza questa classe per inserire informazioni su monumenti o luoghi di rilevanza storica.";
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

    @Override
    public void drawYourSelf(Context context, LinearLayout linFree) {
        EntityViewHelper.addShortEditText(context, linFree, id_fields[0], "Denominazione", "Inserisci la denominazione del luogo storico.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[1], "Comune e Provincia", "Inserisci il comune e la provicina del monumento/luogo.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[2], "Proprietà attuali", "Inserisci una breve descrizione dello stato attuale del monumento/luogo.");
        EntityViewHelper.addLongEditText(context, linFree, id_fields[3], "Proprietà attuali", "Inserisci una breve descrizione dello stato attuale del monumento/luogo.", 3);
        EntityViewHelper.addShortEditText(context, linFree, id_fields[4], "Monumento - Fatto costruire da", "Inserisci il nome di chi ha commissionato la costruzione del monumento.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[5], "Monumento - Perchè lo fece costruire", "Inserisci il motivo che ha portato alla costruzione del monumento.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[6], "Monumento - Materiali", "Inserisci quali sono i materiali utilizzati nella costruzione.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[7], "Monumento - Opere d'arte", "Inserisci la descrizione di eventuali opere d'arte presenti nelle vicinanze.");
        EntityViewHelper.addDatePicker(context, linFree, id_fields[8], "Monumento - Data Costruzione");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[9], "Luogo - Da cosa è scaturito", "Inserisci come mai questo luogo storico è importante.");
        EntityViewHelper.addLongEditText(context, linFree, id_fields[10], "Luogo - Racconto dell'avvenimento", "Inserisci la descrizione dell'avvennimento legato a questo luogo storico.", 3);
        EntityViewHelper.addShortEditText(context, linFree, id_fields[11], "Luogo - Uso attuale", "Inserisci la descrizione di quale utilizzo se ne fa oggi di questo luogo.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[12], "Luogo - Stato di manutenzione", "Inserisci lo stato di conservazione/manutenzione in cui si trova adesso questo luogo.");
        EntityViewHelper.addDatePicker(context, linFree, id_fields[13], "Luogo - Data Avvenimento");
    }

    @Override
    public void saveFields(Context context, LinearLayout linFree) {
        EntityViewHelper.saveLinearLayoutFields(extraFields, id_fields, linFree);
    }

    @Override
    public void restoreFields(Context context, LinearLayout linFree) {
        EntityViewHelper.restoreLinearLayoutFields(extraFields, id_fields, linFree);
    }
}
