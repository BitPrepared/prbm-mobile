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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

/**
 * Class responsible for visualizing a single Prbm
 */
public class PrbmActivity extends AppCompatActivity {

    /** Debug TAG */
    private final static String TAG = "PrbmActivity";

    /** Flag used for context menu - Edit unit */
    private final static int MENU_UNIT_EDIT = 1;
    /** Flag used for context menu - Add unit before */
    private final static int MENU_UNIT_ADD_BEFORE = 2;
    /** Flag used for context menu - Add unit after */
    private final static int MENU_UNIT_ADD_AFTER = 3;
    /** Flag used for context menu - Delete Unit */
    private final static int MENU_UNIT_DELETE = 4;
    /** Flag used for context menu - Obtain GPS */
    private final static int MENU_UNIT_GPS = 5;

    /** Flag used for activity add Entity */
    public final static int ACTIVITY_ADD_ENTITY = 101;
    /** Flag used for activity modify Entity */
    public static final int ACTIVITY_MODIFY_ENTITY = 102;

    /** Reference to Prbm object */
    private Prbm refPrbm = null;

    /** Reference to Units list */
    private ListView lstUnits = null;
    /** Reference to Unit adapter */
    private PrbmUnitAdapter adtUnit = null;

    /** Reference to Edit Text for Minutes */
    private EditText edtMinutes;
    /** Reference to Edit Text for Meters */
    private EditText edtMeters;
    /** Reference to Edit Text for Azimut */
    private EditText edtAzimut;

    /** Reference to value edit dialog */
    private AlertDialog valueDialog = null;

    /** Reference to Location Manager */
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prbm);
        lstUnits = (ListView) findViewById(R.id.lstUnits);

        refPrbm = UserData.getPrbm();
        if (refPrbm != null) {
            adtUnit = new PrbmUnitAdapter(PrbmActivity.this,
                    R.layout.list_units, refPrbm.getUnits());
            lstUnits.setAdapter(adtUnit);
            registerForContextMenu(lstUnits);
        }

        // Building dialog for unit value edit
        AlertDialog.Builder alertValuesBuilder = new AlertDialog.Builder(PrbmActivity.this);
        View viewValuesDialog = this.getLayoutInflater().inflate(
                R.layout.modify_unit_values, null, false);
        edtMinutes = (EditText) viewValuesDialog
                .findViewById(R.id.minutes_input);
        edtMeters = (EditText) viewValuesDialog.findViewById(R.id.meter_input);
        edtAzimut = (EditText) viewValuesDialog.findViewById(R.id.azimut_input);

        alertValuesBuilder.setMessage(getString(R.string.modify_entity_values));
        alertValuesBuilder.setIcon(R.drawable.ic_compass_outline_black_48dp);
        alertValuesBuilder.setView(viewValuesDialog);
        alertValuesBuilder.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertValuesBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update Unit values
                PrbmUnit unit = UserData.getUnit();
                unit.setAzimut(edtAzimut.getText().toString().replace(',','.'));
                unit.setMinutes(edtMinutes.getText().toString().replace(',','.'));
                unit.setMeters(edtMeters.getText().toString().replace(',','.'));
                dialog.dismiss();
                adtUnit.notifyDataSetInvalidated();
            }
        });
        valueDialog = alertValuesBuilder.create();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lstUnits) {
            menu.setHeaderTitle(getString(R.string.menu_prbm_unit));
            menu.add(Menu.NONE, MENU_UNIT_EDIT, 0, getString(R.string.modify_values));
            menu.add(Menu.NONE, MENU_UNIT_ADD_AFTER, 0, getString(R.string.insert_unit_up));
            menu.add(Menu.NONE, MENU_UNIT_ADD_BEFORE, 0, getString(R.string.insert_unit_down));
            menu.add(Menu.NONE, MENU_UNIT_DELETE, 0, getString(R.string.delete_row));
            menu.add(Menu.NONE, MENU_UNIT_GPS, 0, R.string.get_gps_coord);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        if (item.getItemId() == MENU_UNIT_EDIT) {
            // Show value dialog
            PrbmUnit unit = refPrbm.getUnit(info.position);
            UserData.setUnit(unit);
            edtMeters.setText(String.valueOf(unit.getMeter()));
            edtAzimut.setText(String.valueOf(unit.getAzimut()));
            edtMinutes.setText(String.valueOf(unit.getMinutes()));
            valueDialog.show();
        } else if (item.getItemId() == MENU_UNIT_ADD_AFTER) {
            // Add a unit after
            refPrbm.addNewUnits(info.position, false);
            adtUnit.notifyDataSetInvalidated();
        } else if (item.getItemId() == MENU_UNIT_ADD_BEFORE) {
            // Add a unit before
            refPrbm.addNewUnits(info.position, true);
            adtUnit.notifyDataSetInvalidated();
        } else if (item.getItemId() == MENU_UNIT_DELETE) {
            // Delete a unit
            if (refPrbm.canDelete()) {
                refPrbm.deleteUnit(info.position);
                adtUnit.notifyDataSetInvalidated();
            } else {
                // Tip to don't delete last unit
                Toast.makeText(this, getString(R.string.you_cant_delete_last_unit), Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == MENU_UNIT_GPS) {
            PrbmUnit unit = refPrbm.getUnit(info.position);
            if (unit.getLatitude() != 0 && unit.getLongitude() != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Aggiornare coordinate GPS?");
                builder.setMessage(
                    "I dati GPS sono già stati acquisiti per questa osservazione, vuoi sovrascrivere?");
                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.setPositiveButton("Si", (dialog, which) -> {
                    requestGPSCoordinates(unit);
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                requestGPSCoordinates(unit);
            }
        }
        return true;
    }

    private void requestGPSCoordinates(final PrbmUnit unit) {

        Toast.makeText(this, "Acquisizione GPS in corso...", Toast.LENGTH_LONG).show();
        unit.setFlagAcquiringGPS(true);
        adtUnit.notifyDataSetInvalidated();

        //noinspection MissingPermission // FIXME Permission check
        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                unit.setLongitude(location.getLongitude());
                unit.setLatitude(location.getLatitude());
                unit.setFlagAcquiringGPS(false);
                adtUnit.notifyDataSetInvalidated();
                Toast.makeText(PrbmActivity.this, "Coordinate acquisite!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        }, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.prbm_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.exit) {
            // Request exit confirm
            confirmExit();
            return true;
        } else if (id == R.id.save) {
            // Save on disk (serialize)
            UserData.savePrbm(PrbmActivity.this, UserData.getPrbm());
            Toast.makeText(PrbmActivity.this, getString(R.string.save_prbm_successful), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.parameters) {
            // Button for accessing PRBM parameters
            Intent parameters = new Intent(PrbmActivity.this, CreatePrbmActivity.class);
            parameters.putExtra("edit", true);
            startActivity(parameters);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Private method used to display an Alert Dialog asking if user
     * is sure to exit Prbm modify
     */
    private void confirmExit() {
        AlertDialog.Builder build = new AlertDialog.Builder(PrbmActivity.this);
        build.setTitle(R.string.confirmation);
        build.setIcon(R.drawable.ic_alert_black_48dp);
        build.setMessage(R.string.are_you_sure);
        build.setPositiveButton(getString(R.string.save_and_exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserData.savePrbm(PrbmActivity.this, UserData.getPrbm());
                Toast.makeText(PrbmActivity.this, getString(R.string.prbm_save_successful), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        build.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        build.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_ADD_ENTITY || requestCode == ACTIVITY_MODIFY_ENTITY) {
            // Update adapter on activity result
            adtUnit.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Inhibits back button press, and ask if user is sure to back
        confirmExit();
    }
}
