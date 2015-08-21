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
import android.widget.EditText;
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
public class EntityTree extends PrbmEntity {

    private static final int[] id_fields = {1001,1002, 1003, 1004, 1005,1006, 1007, 1008, 1009,1010, 1011};
    private List<String> extraFields = new ArrayList<>();

    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityTree(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityTree
     */
    public EntityTree() {
        this("", "", "");
    }

    @Override
    public JSONObject toJSONObject() {
        return null;
    }

    public String getType(){
        return "Albero/Arbusto";
    }

    @Override
    public String getTypeDescription() {
        return "Utilizza questa classe per inserire avvistamente di alberi, foreste, arbosti o altro tipo di vegetazione";
    }

    @Override
    public int getIdListImage() {
        return R.drawable.background_tree_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.background_tree_button;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_tree;
    }

    @Override
    public void drawYourSelf(Context context, LinearLayout linFree) {
        EntityViewHelper.addShortTextView(context, linFree, id_fields[0], "Nome comune", "Inserire il nome comune della pianta.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[1], "Nome scientifico", "Inserire il nome scientifico della pianta.");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[2], "Altezza media (m): ");
        EntityViewHelper.addLongTextView(context, linFree, id_fields[3], "Portamento", "Inserire il portamento della pianta (espanso, conico, fastigliato, colonnare," +
                " ombrelliforme, ricadente, scadente, prostrato, strisciante, etc.) ", 3);
        EntityViewHelper.addLongTextView(context, linFree, id_fields[4], "Corteccia", "Inserire informazioni sulla corteccia della pianta.", 3);
        EntityViewHelper.addLongTextView(context, linFree, id_fields[5], "Foglie", "Inserire informazioni sulle foglie della pianta (forma, nervature, margine, lamina, picciolo, colore, peli e superficie).", 3);
        EntityViewHelper.addLongTextView(context, linFree, id_fields[6], "Fiori e frutti", "Inserire informazioni sui fiori e sui frutti della pianta.", 3);
        EntityViewHelper.addShortTextView(context, linFree, id_fields[7], "Habitat tipico", "Inserire la descrizione dell'habitat tipico della pianta.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[8], "Distribuzione", "Inserire la descrizione della distribuzione della pianta.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[9], "Proprietà", "Inserire eventuali proprietà di questa pianta.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[10], "Curiosità", "Spazio per eventuali curiosità.");
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
