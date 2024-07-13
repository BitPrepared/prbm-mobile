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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used to render list of Units
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
     * Method to obtain current view in an optimized manner
     * @param position    Position in list
     * @param convertView ConvertView returned at each invocation
     * @param parent      Parent viewgroup
     * @return Just created/rendered view
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
            viewHolder.txtGPS = (TextView) convertView.findViewById(R.id.txtGPS);
            viewHolder.lstFarLeft = (LinearLayout) convertView.findViewById(R.id.lstEntityFarLeft);
            viewHolder.lstFarRight =
                (LinearLayout) convertView.findViewById(R.id.lstEntityFarRight);
            viewHolder.lstNearLeft =
                (LinearLayout) convertView.findViewById(R.id.lstEntityNearLeft);
            viewHolder.lstNearRight =
                (LinearLayout) convertView.findViewById(R.id.lstEntityNearRight);
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
        viewHolder.txtAzimut.setText(c.getString(R.string.azimut) + unit.getAzimuth());
        viewHolder.txtMeters.setText(c.getString(R.string.meters) + unit.getMeter());
        viewHolder.txtMinutes.setText(c.getString(R.string.minutes) + unit.getMinutes());

        if (unit.isFlagAcquiringGPS()){
            viewHolder.txtGPS.setText("Coordinate: Acquisizione...");
            viewHolder.txtGPS.setTextColor(c.getResources().getColor(R.color.black));
        } else if (unit.latitude == 0 && unit.longitude == 0 ){
            viewHolder.txtGPS.setText("Coordinate: Assenti");
            viewHolder.txtGPS.setTextColor(c.getResources().getColor(R.color.red));
        } else {
            viewHolder.txtGPS.setText("Coordinate: "  + unit.latitude + " - " + unit.longitude);
            viewHolder.txtGPS.setTextColor(c.getResources().getColor(R.color.green));
        }
        // Setting onclick listner for add buttons
        Button[]
            arrayBtn =
            {viewHolder.btnFarLeft, viewHolder.btnNearLeft, viewHolder.btnNearRight,
             viewHolder.btnFarRight};
        for (int j = 0; j < arrayBtn.length; j++) {
            final int column = j;
            arrayBtn[j].setOnClickListener(v -> {
                UserData.setColumn(column);
                UserData.setUnit(unit);
                Intent addEntity = new Intent(c, PrbmAddEntityActivity.class);
                ((Activity) c).startActivityForResult(addEntity, PrbmActivity.ACTIVITY_ADD_ENTITY);
            });
        }
//        viewHolder.btnGPS.setBackgroundColor(c.getResources().getColor((unit.getLatitude() == 0) ? R.color.Red : R.color.LightGreen));
//        viewHolder.btnGPS.setOnClickListener(v -> {
//            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//            try {
//                ((Activity) c).startActivityForResult(builder.build((Activity) c), PrbmActivity.ACTIVITY_PLACE_PICKER);
//            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            }
//            if (unit.getLatitude() != 0 && unit.getLongitude() != 0) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(c);
//                builder.setTitle("Aggiornare coordinate GPS?");
//                builder.setMessage(
//                    "I dati GPS sono già stati acquisiti per questa osservazione, vuoi sovrascrivere?");
//                builder.setNegativeButton("No", (dialog, which) -> {
//                    dialog.dismiss();
//                });
//                builder.setPositiveButton("Si", (dialog, which) -> {
//                    requestGPSCoordinates(unit, v);
//                });
//                AlertDialog alert = builder.create();
//                alert.show();
//            } else {
//                requestGPSCoordinates(unit, v);
//            }
//        });

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(5, 0, 5, 5);

        // Array of entities and linear layouts
        List[] arrayEntity = {unit.getFarLeft(), unit.getNearLeft(), unit.getNearRight(), unit.getFarRight()};
        LinearLayout[] arrayLin = {viewHolder.lstFarLeft, viewHolder.lstNearLeft, viewHolder.lstNearRight, viewHolder.lstFarRight};


        for (int i = 0; i < arrayLin.length; i++) {
            List<PrbmEntity> entities = arrayEntity[i];
            int j;
            for (j = 0; j < entities.size(); j++) {
                final PrbmEntity entity = entities.get(j);
                View v = arrayLin[i].getChildAt(j + 1);
                if (v != null && v instanceof Button) {
                    Button b = (Button) v;
                    if (b.getText().toString().contentEquals(entity.getType()))
                        continue; // Button is already present...skip to next
                    else
                        arrayLin[i].removeViewAt(j + 1);
                }

                // If not present, create a new button
                Button b = new Button(c);
                b.setText(entity.getType());
                b.setLayoutParams(param);
                b.setFocusable(false);
                b.setBackgroundResource(entity.getIdButtonImage());
                b.setTextColor(c.getResources().getColor(R.color.white));
                final int column = i;
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        UserData.setColumn(column);
                        UserData.setUnit(unit);
                        UserData.setEntity(entity);

                        // Creating a contextual menu as Item Dialog
                        ArrayList<String> menuItems = new ArrayList<String>();
                        menuItems.add(c.getString(R.string.modify));
                        menuItems.add(c.getString(R.string.delete));

                        // Check if entity can move up or down
                        if (UserData.canMoveUnitUp()) {
                            menuItems.add(c.getString(R.string.move_up));
                        }
                        if (UserData.canMoveUnitDown()) {
                            menuItems.add(c.getString(R.string.move_down));
                        }

                        final String[] items = menuItems.toArray(new String[menuItems.size()]);
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setTitle(c.getString(R.string.entity_menu));
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Handling item selection
                                if (item == 0) {
                                    Intent addEntity = new Intent(c, EntityActivity.class);
                                    addEntity.putExtra("edit", true);
                                    ((Activity) c).startActivityForResult(addEntity, PrbmActivity.ACTIVITY_MODIFY_ENTITY);
                                    dialog.dismiss();
                                } else if (item == 1) {

                                    // Alert dialog to check if sure to delete
                                    AlertDialog.Builder alert = new AlertDialog.Builder(c);
                                    alert.setTitle(c.getString(R.string.confirm_delete));
                                    alert.setMessage(c.getString(R.string.are_you_sure_delete_entity));
                                    alert.setIcon(R.drawable.ic_alert_black_48dp);
                                    alert.setPositiveButton(R.string.delete,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int whichButton) {
                                                    unit.deleteEntity(entity, column);
                                                    PrbmUnitAdapter.this.notifyDataSetChanged();
                                                    dialog.dismiss();
                                                }
                                            });
                                    alert.setNegativeButton(R.string.abort,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int whichButton) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    dialog.dismiss();
                                    alert.show();
                                } else if (items[item].contentEquals(c.getString(R.string.move_up))) {
                                    // Moving entity up
                                    unit.moveEntity(entity, column, false);
                                    PrbmUnitAdapter.this.notifyDataSetChanged();
                                    dialog.dismiss();
                                } else if (items[item].contentEquals(c.getString(R.string.move_down))) {
                                    // Moving entity down
                                    unit.moveEntity(entity, column, true);
                                    PrbmUnitAdapter.this.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                arrayLin[i].addView(b);
            }
            try {
                // Remove remaining views (if exceed)
                arrayLin[i].removeViews(j + 1, arrayLin[i].getChildCount() - (j + 1));
            } catch (Exception e) {
            }
        }

        return convertView;
    }

    /**
     * Viewholder class used to improve performance
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
        /** Reference to Coordinates */
        public TextView txtGPS;
    }
}
