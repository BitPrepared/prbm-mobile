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
 * @author Nicola Corti
 */
public class EntityCuriosity extends PrbmEntity {

    private static final int[] id_fields = {1001, 1002};
    private List<String> extraFields = new ArrayList<>();


    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityCuriosity(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityFauna
     */
    public EntityCuriosity() {
        this("", "", "");
    }

    @Override
    public JSONObject toJSONObject() {
        return null;
    }

    public String getType() {
        return "Curiosità";
    }

    @Override
    public String getTypeDescription() {
        return "Utilizza questa classe per inserire ulteriori curiosità oppure osservazioni che non rientrano nelle altre classi.";
    }

    @Override
    public int getIdListImage() {
        return R.drawable.background_curiosity_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_curiosity;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_curiosity;
    }

    @Override
    public void drawYourSelf(Context context, LinearLayout linFree) {
        EntityViewHelper.addLongTextView(context, linFree, id_fields[0], "Perchè è importante?", "Come mai è importante riportare questa curiosità?", 3);
        EntityViewHelper.addLongTextView(context, linFree, id_fields[1], "Impressioni/Stato d'animo", "Inserire una breve descrizione delle impressioni o dello stato d'animo.", 3);
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
