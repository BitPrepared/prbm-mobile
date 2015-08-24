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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

/**
 * Class responsible for visualizing a single Prbm
 * @author Nicola Corti
 */
public class PrbmActivity extends Activity implements OnLongClickListener,
        OnClickListener {

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

    /** Flag used for activity add Entity */
    public final static int ACTIVITY_ADD_ENTITY = 101;
    public static final int ACTIVITY_MODIFY_ENTITY = 102;

    /** Riference to Prbm object */
    private Prbm refPrbm = null;

    /** Riference to Units list */
    private ListView lstUnits = null;

    private PrbmUnitAdapter adtUnit = null;

    private EditText edtMinutes;
    private EditText edtMeters;
    private EditText edtAzimut;

    private AlertDialog valueDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prbm);
        lstUnits = (ListView) findViewById(R.id.lstUnits);

        refPrbm = UserData.getInstance().getPrbm();
        if (refPrbm != null) {
            adtUnit = new PrbmUnitAdapter(PrbmActivity.this,
                    R.layout.list_units, refPrbm.getUnits());
            lstUnits.setAdapter(adtUnit);
            registerForContextMenu(lstUnits);
        }

        AlertDialog.Builder alertValuesBuilder = new AlertDialog.Builder(PrbmActivity.this);
        View viewValuesDialog = this.getLayoutInflater().inflate(
                R.layout.modify_unit_values, null, false);
        edtMinutes = (EditText) viewValuesDialog
                .findViewById(R.id.minutes_input);
        edtMeters = (EditText) viewValuesDialog.findViewById(R.id.meter_input);
        edtAzimut = (EditText) viewValuesDialog.findViewById(R.id.azimut_input);

        alertValuesBuilder.setMessage("Modifica valori riga");
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
                PrbmUnit unit = UserData.getInstance().getUnit();
                unit.setAzimut(edtAzimut.getText().toString());
                unit.setMinutes(edtMinutes.getText().toString());
                unit.setMeters(edtMeters.getText().toString());
                dialog.dismiss();
            }
        });
        valueDialog = alertValuesBuilder.create();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lstUnits) {
            menu.setHeaderTitle(getString(R.string.menu_prbm_unit));
            menu.add(Menu.NONE, MENU_UNIT_EDIT, 0, getString(R.string.modify_values));
            menu.add(Menu.NONE, MENU_UNIT_ADD_AFTER, 0, getString(R.string.insert_unit_up));
            menu.add(Menu.NONE, MENU_UNIT_ADD_BEFORE, 0, getString(R.string.insert_unit_down));
            menu.add(Menu.NONE, MENU_UNIT_DELETE, 0, getString(R.string.delete_row));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();

        if (item.getItemId() == MENU_UNIT_EDIT) {
            PrbmUnit unit = refPrbm.getUnit(info.position);
            UserData.getInstance().setUnit(unit);
            edtMeters.setText(String.valueOf(unit.getMeter()));
            edtAzimut.setText(String.valueOf(unit.getAzimut()));
            edtMinutes.setText(String.valueOf(unit.getMinutes()));
            valueDialog.show();
        } else if (item.getItemId() == MENU_UNIT_ADD_AFTER) {
            refPrbm.addNewUnits(info.position, false);
            adtUnit.notifyDataSetInvalidated();
        } else if (item.getItemId() == MENU_UNIT_ADD_BEFORE) {
            refPrbm.addNewUnits(info.position, true);
            adtUnit.notifyDataSetInvalidated();
        } else if (item.getItemId() == MENU_UNIT_DELETE) {
            if (refPrbm.canDelete()) {
                refPrbm.deleteUnit(info.position);
                adtUnit.notifyDataSetInvalidated();
            } else {
                Toast.makeText(this, getString(R.string.you_cant_delete_last_unit), Toast.LENGTH_SHORT).show();
            }
        }
        return true;
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
            confirmExit();
            return true;
        } else if (id == R.id.save){
            UserData.getInstance().savePrbm(PrbmActivity.this);
            Toast.makeText(PrbmActivity.this, getString(R.string.save_prbm_successful), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.parameters){
            Intent parameters = new Intent(PrbmActivity.this, CreatePrbmActivity.class);
            parameters.putExtra("edit", true);
            startActivity(parameters);
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmExit() {
        AlertDialog.Builder build = new AlertDialog.Builder(PrbmActivity.this);
        build.setTitle(R.string.confirmation);
        build.setIcon(R.drawable.ic_alert_black_48dp);
        build.setMessage(R.string.are_you_sure);
        build.setPositiveButton(getString(R.string.save_and_exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserData.getInstance().savePrbm(PrbmActivity.this);
                Toast.makeText(PrbmActivity.this, "Salvataggio del PRBM riuscito", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        build.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        build.setNeutralButton(getString(R.string.exit_without_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        build.show();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_ADD_ENTITY || requestCode == ACTIVITY_MODIFY_ENTITY) {
            adtUnit.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }
}
