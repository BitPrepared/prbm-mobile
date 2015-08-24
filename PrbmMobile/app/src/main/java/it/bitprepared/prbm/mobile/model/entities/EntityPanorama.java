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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.activity.EntityViewHelper;
import it.bitprepared.prbm.mobile.model.PrbmEntity;

/**
 * Entity used to represent an observation of a panorama
 * @author Nicola Corti
 */
public class EntityPanorama extends PrbmEntity {

    /** List of View IDs */
    private static final int[] id_fields = {1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008};
    /** List of Extra fields values */
    private List<String> extraFields = new ArrayList<>();


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
    public JSONObject toJSONObject() {
        return null;
    }

    @Override
    public String getType() {
        return "Ambiente Naturale";
    }

    @Override
    public String getTypeDescription() {
        return "Utilizza questa classe per inserire informazioni sull'ambiente naturale circostante e sui panorami ammirati.";
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

    @Override
    public void drawYourSelf(Context context, LinearLayout linFree) {
        EntityViewHelper.addShortEditText(context, linFree, id_fields[0], "Località", "Inserire il nome della località dell'ambiente osservato.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[1], "Tipo Ambiente", "Inserire la tipologia di ambiente osservato.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[2], "Descrizione della flora", "Inserire una breve descrizione della flora circostante.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[3], "Impatto dell'uomo", "Descrivere quale è stato l'impatto dell'uomo sull'ambiente osservato.");
        EntityViewHelper.addLongEditText(context, linFree, id_fields[4], "Cosa ti è piaciuto di più?", "Indica ciò che ti è piaciuto di più in questo ambiente naturale.", 3);
        EntityViewHelper.addLongEditText(context, linFree, id_fields[5], "Cosa ti è piaciuto di meno?", "Indica ciò che ti è piaciuto di meno in questo ambiente naturale.", 3);
        EntityViewHelper.addLongEditText(context, linFree, id_fields[6], "Senzazioni personali positive", "Indica se la visione di questo ambiente di ha suscitato senzazioni positive.", 3);
        EntityViewHelper.addLongEditText(context, linFree, id_fields[7], "Senzazioni personali nevative", "Indica se la visione di questo ambiente di ha suscitato senzazioni negative.", 3);
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
