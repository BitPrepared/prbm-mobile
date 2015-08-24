package it.bitprepared.prbm.mobile.activity;

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


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;

/**
 * @author Nicola Corti
 */
public class ListPrbmActivity extends Activity implements OnItemClickListener {

    /** Rif to Listview of Prbms */
    private ListView lstPrbms;

    /** Ref to PRBM Adapter */
    private PrbmAdapter adtPrbm;

    private static final int MENU_EDIT = 1;
    private static final int MENU_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Imposto il ritorno con ActionBar
        ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_list_prbm);

        // Recupero i riferimenti alle views
        lstPrbms = (ListView) findViewById(R.id.listPrbm);

        registerForContextMenu(lstPrbms);
        lstPrbms.setOnItemClickListener(this);

        TextView empty = new TextView(ListPrbmActivity.this);
        empty.setText(getString(R.string.no_prbm_to_show));
        empty.setTypeface(null, Typeface.ITALIC);
        empty.setTextSize(20);
        ListView.LayoutParams params = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        empty.setLayoutParams(params);
        empty.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        ((ViewGroup)lstPrbms.getParent()).addView(empty);
        lstPrbms.setEmptyView(empty);

        List<Prbm> prbms = UserData.getInstance().getAllPrbm();

        adtPrbm = new PrbmAdapter(ListPrbmActivity.this, R.layout.list_prbm, prbms);
        lstPrbms.setAdapter(adtPrbm);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle(getString(R.string.prbm_operations));
        menu.add(Menu.NONE, MENU_EDIT, 0, R.string.modify);
        menu.add(Menu.NONE, MENU_DELETE, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final Prbm pressed = (Prbm) lstPrbms.getAdapter().getItem(info.position);


        if (item.getItemId() == MENU_EDIT) {

            UserData.getInstance().setPrbm(pressed);
            Intent i = new Intent(ListPrbmActivity.this, PrbmActivity.class);
            startActivity(i);
            finish();
            return true;

        } else if (item.getItemId() == MENU_DELETE) {
            AlertDialog.Builder delete_dialog = new AlertDialog.Builder(ListPrbmActivity.this);
            delete_dialog.setTitle(getString(R.string.delete_prbm));
            delete_dialog.setMessage(getString(R.string.are_you_sure_to_delete_prbm));
            delete_dialog.setIcon(R.drawable.ic_alert_black_48dp);

            delete_dialog.setNegativeButton(R.string.abort, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            delete_dialog.setPositiveButton(R.string.delete, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UserData.getInstance().deletePrbm(ListPrbmActivity.this, pressed);
                    adtPrbm.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            delete_dialog.show();
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast.makeText(ListPrbmActivity.this, getString(R.string.long_press_a_prbm_to_open_menu), Toast.LENGTH_SHORT).show();
    }
}