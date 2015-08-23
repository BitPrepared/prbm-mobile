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
import it.bitprepared.prbm.mobile.activity.EntityActivity;
import it.bitprepared.prbm.mobile.activity.EntityViewHelper;
import it.bitprepared.prbm.mobile.model.PrbmEntity;

/**
 * @author Nicola Corti
 */
public class EntityFlower extends PrbmEntity {

    private static final int[] id_fields = {1001,1002, 1003, 1004, 1005,1006, 1007, 1008, 1009};
    private List<String> extraFields = new ArrayList<>();


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

    @Override
    public JSONObject toJSONObject() {
        return null;
    }

    public String getType(){
        return "Fiore/Erba";
    }

    @Override
    public String getTypeDescription() {
        return "Utilizza questa classe per inserire avvistamente di fiori o erba.";
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
        return R.drawable.background_flower;
    }

    @Override
    public void drawYourSelf(Context context, LinearLayout linFree) {
        EntityViewHelper.addShortTextView(context, linFree, id_fields[0], "Nome comune", "Inserire il nome comune del fiore.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[1], "Nome scientifico", "Inserire il nome scientifico del fiore.");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[2], "Numero petali: ");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[3], "Altezza media (m): ");
        EntityViewHelper.addLongTextView(context, linFree, id_fields[4], "Descrizione foglie", "Inserire una breve descrizione delle foglie del fiore.", 3);
        EntityViewHelper.addShortTextView(context, linFree, id_fields[5], "Habitat tipico", "Inserire la descrizione dell'habitat tipico della pianta.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[6], "Distribuzione", "Inserire la descrizione della distribuzione della pianta.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[7], "Proprietà", "Inserire eventuali proprietà di questa pianta.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[8], "Curiosità", "Spazio per eventuali curiosità.");
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
