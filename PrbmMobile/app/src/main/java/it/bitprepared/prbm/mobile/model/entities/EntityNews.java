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
 * Entity used to represent a news or a report
 * @author Nicola Corti
 */
public class EntityNews extends PrbmEntity {

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
    public EntityNews(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityFauna
     */
    public EntityNews() {
        this("", "", "");
    }

    @Override
    public JSONObject toJSONObject() {
        return null;
    }

    @Override
    public String getType() {
        return "Fatto di cronaca";
    }

    @Override
    public String getTypeDescription() {
        return "Utilizza questa classe per riportare notizie o fatti di cronaca del posto.";
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

    @Override
    public void drawYourSelf(Context context, LinearLayout linFree) {
        EntityViewHelper.addShortEditText(context, linFree, id_fields[0], "Luogo", "Inserire il luogo dove si è svolto il fatto di cronaca.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[1], "Persone coinvolte", "Inserire l'elenco delle persone coinvolte.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[2], "Cosa è successo?", "Inserire la descrizione del fatto di cronaca.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[3], "Perchè?", "Inserire le motivazioni o ciò che ha provocato il fatto.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[4], "Se negativo, poteva essere evitato?", "Inserire se, ed in quali circostanze, il fatto di cronaca negativo poteva essere evitato.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[5], "Perchè?", "Inserire le motivazioni o ciò che ha provocato il fatto.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[6], "Conseguenze", "Inserire le conseguenze del fatto di cronaca.");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[7], "Cosa hai imparato?", "Inserire cosa hai imparato da questo fatto di cronaca.");
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
