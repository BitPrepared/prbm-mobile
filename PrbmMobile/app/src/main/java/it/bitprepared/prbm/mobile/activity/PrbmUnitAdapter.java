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

package it.bitprepared.prbm.mobile.activity;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

/**
 * Adapter used to render list of Units
 * @author Nicola Corti
 */
public class PrbmUnitAdapter extends ArrayAdapter<PrbmUnit> {

    /** Execution context */
    private Context c;

    /**
     * Base constructor
     * @param context  Execution context
     * @param resource Related resource
     * @param objects  List of PrbmUnits
     */
    public PrbmUnitAdapter(Context context, int resource, List<PrbmUnit> objects) {
        super(context, resource, objects);
        this.c = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewOptimize(position, convertView, parent);
    }

    /**
     * Metoto per ottenere la view in modo ottimizzato
     * @param position    Posizione nella lista
     * @param convertView Vista che viene ritornata ad ogni invocazione
     * @param parent      ViewGroup padre della vista
     * @return La nuova vista disegnata
     */
    public View getViewOptimize(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {

            // Se la convertView e' nulla la devo caricare la prima volta
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_units, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txtAzimut = (TextView) convertView.findViewById(R.id.txtAzimut);
            viewHolder.txtMeters = (TextView) convertView.findViewById(R.id.txtMeters);
            viewHolder.txtMinutes = (TextView) convertView.findViewById(R.id.txtMinutes);
            viewHolder.lstFarLeft = (LinearLayout) convertView.findViewById(R.id.lstEntityFarLeft);
            viewHolder.lstFarRight = (LinearLayout) convertView.findViewById(R.id.lstEntityFarRight);
            viewHolder.lstNearLeft = (LinearLayout) convertView.findViewById(R.id.lstEntityNearLeft);
            viewHolder.lstNearRight = (LinearLayout) convertView.findViewById(R.id.lstEntityNearRight);

            // Set the tag tag
            convertView.setTag(viewHolder);
        } else {
            // Restore tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PrbmUnit unit = getItem(position);

        viewHolder.txtAzimut.setText(c.getString(R.string.azimut) + unit.getAzimut());
        viewHolder.txtMeters.setText(c.getString(R.string.meters) + unit.getMeter());
        viewHolder.txtMinutes.setText(c.getString(R.string.minutes) + unit.getMinutes());

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(5, 0, 5, 5);

        for (PrbmEntity farLeft: unit.getFarLeft()){
            Button b = new Button(c);
            b.setText(farLeft.getType());
            b.setLayoutParams(param);
            b.setFocusable(false);
            viewHolder.lstFarLeft.addView(b);
        }
        for (PrbmEntity nearLeft: unit.getNearLeft()){
            Button b = new Button(c);
            b.setText(nearLeft.getType());
            b.setLayoutParams(param);
            b.setFocusable(false);
            viewHolder.lstNearLeft.addView(b);
        }
        for (PrbmEntity nearRight: unit.getNearRight()){
            Button b = new Button(c);
            b.setText(nearRight.getType());
            b.setLayoutParams(param);
            b.setFocusable(false);
            viewHolder.lstNearRight.addView(b);
        }
        for (PrbmEntity farRight: unit.getFarRight()){
            Button b = new Button(c);
            b.setText(farRight.getType());
            b.setLayoutParams(param);
            b.setFocusable(false);
            viewHolder.lstFarRight.addView(b);
        }
        return convertView;
    }

    /**
     * Viewholder class used to improve performance
     * @author Nicola Corti
     */
    private class ViewHolder {
        /** Reference to Azimut label */
        public TextView txtAzimut;
        /** Reference to Meters label */
        public TextView txtMeters;
        /** Reference to Steps label */
        public TextView txtMinutes;
        /** Reference to far left list */
        public LinearLayout lstFarLeft;
        /** Reference to near left list */
        public LinearLayout lstNearLeft;
        /** Reference to near right list */
        public LinearLayout lstNearRight;
        /** Reference to far right list */
        public LinearLayout lstFarRight;
    }
}
