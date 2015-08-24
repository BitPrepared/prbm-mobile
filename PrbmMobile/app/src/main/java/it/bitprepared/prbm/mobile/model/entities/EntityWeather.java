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
 * Entity used to represent a weather/forecast observation
 * @author Nicola Corti
 */
public class EntityWeather extends PrbmEntity {

    /** List of View IDs */
    private static final int[] id_fields = {1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008,
            1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020};
    /** List of Extra fields values */
    private List<String> extraFields = new ArrayList<>();


    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public EntityWeather(String description, String caption, String timestamp) {
        super(description, caption, timestamp);
    }

    /**
     * Base constructor for an empty EntityFauna
     */
    public EntityWeather() {
        this("", "", "");
    }

    @Override
    public JSONObject toJSONObject() {
        return null;
    }

    @Override
    public String getType() {
        return "Meteo";
    }

    @Override
    public String getTypeDescription() {
        return "Utilizza questa classe per inserire informazioni sulle condizioni atmosferiche osservate.";
    }

    @Override
    public int getIdListImage() {
        return R.drawable.background_forecast_list;
    }

    @Override
    public int getIdButtonImage() {
        return R.drawable.button_forecast;
    }

    @Override
    public int getIdBackImage() {
        return R.drawable.background_forecast;
    }

    @Override
    public void drawYourSelf(Context context, LinearLayout linFree) {
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[0], "Temperatura (C°): ");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[1], "Umidità (%): ");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[2], "Pressione (mBar): ");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[3], "Velocità del vento (Km/h): ");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[4], "Direzione del vento", "Inserire la direzione del vento (Nord, Sud, Est, Ovest).");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[5], "Precipitazioni", "Inserire una breve descrizione delle precipitazioni attuali.");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[6], "Quantità di pioggia (mm): ");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[7], "Acidità dell'acqua (Ph): ");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[8], "Neve", "Inserire una breve descrizione della neve (intensità, tipo di fiocco, torbidità dell'acqua)");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[9], "Neve, acidità dell'acqua distillata (Ph): ");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[10], "Grandine", "Inserire una breve descrizione della grandine (intensità, tipo di fiocco, torbidità dell'acqua)");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[11], "Grandine, diametro (cm): ");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[12], "Grandine, acidità dell'acqua (Ph): ");
        EntityViewHelper.addLongEditText(context, linFree, id_fields[13], "Eventi particolari", "Inserire eventuali eventi particolari (trombe d'aria, tornadi, cicloni, tempeste)", 3);
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[14], "Sole, angolazione (°): ");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[15], "Sole, eventuali anomalie", "Inserire eventuali anomalie solari quali eclissi parziali o totali");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[16], "Nebbia, visibilità (m): ");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[17], "Nebbia, tipo di formazione", "Inserire il tipo di formazione della nebbia (nebbia, nuvole, evaporazione)");
        EntityViewHelper.addNumericTextView(context, linFree, id_fields[18], "Nubi, velocità di spostamento: ");
        EntityViewHelper.addShortEditText(context, linFree, id_fields[19], "Nubi, tipologia", "Inserire la tipologia delle nubi osservate (nembi, cumoli, cirri, etc.)");
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
