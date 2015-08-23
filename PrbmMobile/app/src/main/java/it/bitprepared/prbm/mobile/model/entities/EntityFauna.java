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
public class EntityFauna extends PrbmEntity {

    private static final int[] id_fields = {1001,1002, 1003, 1004, 1005,1006, 1007, 1008, 1009,1010, 1011, 1012, 1013, 1014, 1015};
    private List<String> extraFields = new ArrayList<>();


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
     * Base constructor for an empty EntityFauna
     */
    public EntityFauna() {
        this("", "", "");
    }

    @Override
    public JSONObject toJSONObject() {
        return null;
    }

    public String getType(){
        return "Fauna";
    }

    @Override
    public String getTypeDescription() {
        return "Utilizza questa classe per inserire avvistamente di animali, nidi, insetti o altri avvistamenti relativi alla fauna.";
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

    @Override
    public void drawYourSelf(Context context, LinearLayout linFree) {
        EntityViewHelper.addShortTextView(context, linFree, id_fields[0], "Nome comune", "Inserire il nome comune dell'animale.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[1], "Nome scientifico", "Inserire il nome scientifico dall'animale.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[2], "Dimensione", "Inserire la dimensione tipica dell'animale.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[3], "Corteggiamento e riproduzione", "Inserire dettagli sulle abitudini di corteggiamento e riproduzione della specie.");
        EntityViewHelper.addLongTextView(context, linFree, id_fields[4], "Tracce/Segni di presenza", "Inserire eventuali ritrovamenti di tracce o di segni di presenza dell'animale.", 3);
        EntityViewHelper.addLongTextView(context, linFree, id_fields[5], "Escrementi", "Inserire dettagli sugli escrementi dell'animale.", 3);
        EntityViewHelper.addLongTextView(context, linFree, id_fields[6], "I piccoli e la prole", "Inserire dettagli sull'accudimento della prole da parte dell'animale.", 3);
        EntityViewHelper.addShortTextView(context, linFree, id_fields[7], "Alimentazione", "Informazioni sulle abitudini alimentari dell'animale.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[8], "Posizione nella catena alimentare", "Inserire la posizione che l'animale ricopre all'interno della catena alimentare.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[9], "Tane/Nidi/Rifugi", "Inserire eventuali dettagli su avvistamenti di tane, nidi o rifugi dell'animale.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[10], "Attacco/Difesa/Mimetismo", "Inserire dettagli sulle tecniche di attacco, difesa o mimetismo utilizzate dall'animale.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[11], "Habitat tipico", "Inserire la descrizione dell'habitat tipico dell'animale.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[12], "Distribuzione", "Inserire la descrizione della distribuzione dell'animale.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[13], "Territorialità", "Inserire eventuali informazioni sulla territorialità della specie.");
        EntityViewHelper.addShortTextView(context, linFree, id_fields[14], "Curiosità", "Spazio per eventuali curiosità.");
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
