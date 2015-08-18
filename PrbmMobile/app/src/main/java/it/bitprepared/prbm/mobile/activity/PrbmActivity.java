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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;

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

    /** Riference to Prbm object */
    private Prbm refPrbm = null;

    /** Riference to Units list */
    private ListView lstUnits = null;

    private PrbmUnitAdapter adtUnit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting up Home back button
        ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_prbm);
        lstUnits = (ListView)findViewById(R.id.lstUnits);

        refPrbm = UserData.getInstance().getPrbm();
        if (refPrbm != null) {
            adtUnit = new PrbmUnitAdapter(PrbmActivity.this,
                    R.layout.list_units, refPrbm.getUnits());
            lstUnits.setAdapter(adtUnit);
            registerForContextMenu(lstUnits);
            Log.d(TAG, "Registred for context menu");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lstUnits) {
            menu.setHeaderTitle(getString(R.string.menu_prbm_unit));
            menu.add(Menu.NONE, MENU_UNIT_EDIT, 0, "Modifica valori");
            menu.add(Menu.NONE, MENU_UNIT_ADD_AFTER, 0, "Inserisci riga sopra");
            menu.add(Menu.NONE, MENU_UNIT_ADD_BEFORE, 0, "Inserisci riga sotto");
            menu.add(Menu.NONE, MENU_UNIT_DELETE, 0, "Elimina riga");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();

        if (item.getItemId() == MENU_UNIT_EDIT) {

            /** SHOW A DIALOG */

        } else if (item.getItemId() == MENU_UNIT_ADD_AFTER) {
            refPrbm.addNewUnits(info.position, true);
            adtUnit.notifyDataSetChanged();
        } else if (item.getItemId() == MENU_UNIT_ADD_BEFORE) {
            refPrbm.addNewUnits(info.position, false);
            adtUnit.notifyDataSetChanged();
        } else if (item.getItemId() == MENU_UNIT_DELETE) {
            if (refPrbm.canDelete()) {
                refPrbm.deleteUnit(info.position);
                adtUnit.notifyDataSetChanged();
            } else{
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
        if (id == R.id.discard || id == android.R.id.home) {

            AlertDialog.Builder build = new AlertDialog.Builder(PrbmActivity.this);
            build.setTitle(R.string.confirmation);
            build.setMessage(R.string.are_you_sure);
            build.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            build.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            build.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onClick(View v) {
    }
}
