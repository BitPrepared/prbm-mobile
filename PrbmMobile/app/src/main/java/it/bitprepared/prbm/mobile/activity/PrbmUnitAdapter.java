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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

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
    public View getViewOptimize(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        final PrbmUnit unit = getItem(position);

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
            viewHolder.btnFarLeft = (Button) convertView.findViewById(R.id.btnAddFarLeft);
            viewHolder.btnFarRight = (Button) convertView.findViewById(R.id.btnAddFarRight);
            viewHolder.btnNearLeft = (Button) convertView.findViewById(R.id.btnAddNearLeft);
            viewHolder.btnNearRight = (Button) convertView.findViewById(R.id.btnAddNearRight);

            // Set the tag tag
            convertView.setTag(viewHolder);
        } else {
            // Restore tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtAzimut.setText(c.getString(R.string.azimut) + unit.getAzimut());
        viewHolder.txtMeters.setText(c.getString(R.string.meters) + unit.getMeter());
        viewHolder.txtMinutes.setText(c.getString(R.string.minutes) + unit.getMinutes());

        Button[] arrayBtn = {viewHolder.btnFarLeft, viewHolder.btnNearLeft, viewHolder.btnNearRight, viewHolder.btnFarRight};
        for(int j = 0; j < arrayBtn.length; j++){
            final int column = j;
            arrayBtn[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserData.getInstance().setColumn(column);
                    UserData.getInstance().setUnit(unit);
                    Intent addEntity = new Intent(c, PrbmAddEntityActivity.class);
                    ((Activity)c).startActivityForResult(addEntity, PrbmActivity.ACTIVITY_ADD_ENTITY);
                }
            });
        }

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(5, 0, 5, 5);

        List[] arrayEntity = {unit.getFarLeft(), unit.getNearLeft(), unit.getNearRight(), unit.getFarRight()};
        LinearLayout[] arrayLin = {viewHolder.lstFarLeft, viewHolder.lstNearLeft, viewHolder.lstNearRight, viewHolder.lstFarRight};


        for(int i = 0; i < arrayLin.length; i++){
            List<PrbmEntity> entities = arrayEntity[i];
            int j;
            for (j = 0; j < entities.size(); j++){
                final PrbmEntity entity = entities.get(j);
                View v = arrayLin[i].getChildAt(j+1);
//                Log.d("ADAPTER", "Lin " + i + " " + arrayLin[i].getChildCount());
                if (v != null && v instanceof Button){
                    Button b = (Button)v;
//                    Log.d("ADAPTER", "Running " + position + " Column " + i + " Entity " + j + " " +b.getText().toString() + " " + entity.getType());
                    if (b.getText().toString().contentEquals(entity.getType()))
                        continue; // Button is already present...skip to next
                    else
                        arrayLin[i].removeViewAt(j+1);
                }
                Button b = new Button(c);
                b.setText(entity.getType());
                b.setLayoutParams(param);
                b.setFocusable(false);
                b.setBackgroundResource(entity.getIdButtonImage());
                b.setTextColor(c.getResources().getColor(R.color.White));
                final int column = i;
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserData.getInstance().setColumn(column);
                        UserData.getInstance().setUnit(unit);
                        UserData.getInstance().setEntity(entity);
                        Intent addEntity = new Intent(c, EntityActivity.class);
                        addEntity.putExtra("edit", true);
                        ((Activity)c).startActivityForResult(addEntity, PrbmActivity.ACTIVITY_MODIFY_ENTITY);
                    }
                });
                arrayLin[i].addView(b);
            }
            try {
                arrayLin[i].removeViews(j + 1, arrayLin[i].getChildCount() - (j + 1));
            } catch (Exception e ) {
//                Log.d("ADAPTER", "Lin: " + i + " j+1 " + (j+1) + " " + arrayLin[i].getChildCount());
            }
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
        /** Reference to far left button */
        public Button btnFarLeft;
        /** Reference to near left button */
        public Button btnNearLeft;
        /** Reference to near right button */
        public Button btnNearRight;
        /** Reference to far right button */
        public Button btnFarRight;
    }
}
