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
 * Entity used to represent an interview to a specific person
 * @author Nicola Corti
 */
public class EntityInterview extends PrbmEntity {

    /** List of View IDs */
    private static final int[] id_fields = {1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008,
            1009, 1010, 1011, 1012};
    /** List of Extra fields values */
    private List<String> extraFields = new ArrayList<>();


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

    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("entity-type", "interview");
            jsonObject.put("description", super.getDescription());
            jsonObject.put("caption", super.getCaption());
            jsonObject.put("minutes", super.getTimestamp());

            for(int i = 0; i < extraFields.size(); i++)
                jsonObject.put("interview-" + i,extraFields);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String getType() {
        return "Intervista";
    }

    @Override
    public String getTypeDescription() {
        return "Utilizza questa classe per inserire report o interviste alle persone incontrate.";
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
        return R.drawable.background_interview;
    }

    @Override
    public void drawYourSelf(Context context, LinearLayout linFree) {
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[0], "Età: ");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[1], "Professione", "Inserire la professione dell'intervistato.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[2], "Stato civile", "Inserire lo stato civile dell'intervistato.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[3], "Provenienza", "Indicare se l'intervistato è un abitante del luogo oppure è un forestiero.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[4], "Ruolo", "Indicare in quale veste il soggetto è stato intervistato.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[5], "Scopo dell'intervista", "Indicare quale è il motivo/scopo di questa intervista.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[6], "Come si è arrivati a lui", "Indicare attraverso quali contatti si è arrivati al soggetto intervistato.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[7], "Stato d'animo", "Indicare lo stato d'animo dell'intervistato.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[8], "Gradimento", "Indicare il grado di gradimento dell'intervistato.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[9], "Affidabilità", "Indicare lo stato civile dell'intervistato.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[10], "Rapporto con l'intervistato", "Indicare che genere di rapporto si è avuto con l'intervistato.");
        EntityViewHelper.addLongEditText(context, linFree, id_fields[11], "Trascrizione dell'intervista", "Riportare la trascrizione integrale dell'intervista.", 5);
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
