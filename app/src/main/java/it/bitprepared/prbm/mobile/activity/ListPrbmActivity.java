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

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;

import java.util.List;

/**
 * Activity responsible of PRBM list visualization
 */
public class ListPrbmActivity extends AppCompatActivity implements OnItemClickListener {

    /** Rif to Listview of Prbms */
    private ListView lstPrbms;

    /** Ref to PRBM Adapter */
    private PrbmAdapter adtPrbm;

    /** Menu ID for edit operation */
    private static final int MENU_EDIT = 1;
    /** Menu ID for delete operation */
    private static final int MENU_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting action bar home button
        ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_list_prbm);

        // Inflating views
        lstPrbms = findViewById(R.id.listPrbm);

        lstPrbms.setOnItemClickListener(this);

        // Setting list empty view
        TextView empty = new TextView(ListPrbmActivity.this);
        empty.setText(getString(R.string.no_prbm_to_show));
        empty.setTypeface(null, Typeface.ITALIC);
        empty.setTextSize(20);
        ListView.LayoutParams params = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        empty.setLayoutParams(params);
        empty.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        ((ViewGroup) lstPrbms.getParent()).addView(empty);
        lstPrbms.setEmptyView(empty);

        // Crating prbm adapter
        List<Prbm> prbms = UserData.getPrbmList();
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
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        final Prbm pressed = (Prbm) lstPrbms.getAdapter().getItem(position);

        AlertDialog.Builder whatToDoDialog = new AlertDialog.Builder(ListPrbmActivity.this);
        whatToDoDialog.setTitle("Cosa vuoi fare con questo PRBM?");
        whatToDoDialog.setMessage("Vuoi cancellare o modificare il PRBM?");
        whatToDoDialog.setNegativeButton("Cancellare", (dialog, which) -> {
            // Delete PRBM alert dialog
            dialog.dismiss();
            AlertDialog.Builder delete_dialog = new AlertDialog.Builder(ListPrbmActivity.this);
            delete_dialog.setTitle(getString(R.string.delete_prbm));
            delete_dialog.setMessage(getString(R.string.are_you_sure_to_delete_prbm));
            delete_dialog.setIcon(R.drawable.ic_alert_black_48dp);
            delete_dialog.setNegativeButton(R.string.abort, (dialog1, which1) -> dialog1.dismiss());

            delete_dialog.setPositiveButton(R.string.delete, (dialog12, which12) -> {
                UserData.deletePrbm(ListPrbmActivity.this, pressed);
                adtPrbm.notifyDataSetChanged();
                dialog12.dismiss();
            });
            delete_dialog.show();
        });
        whatToDoDialog.setPositiveButton("Modificare", (dialog, which) -> {
            UserData.setPrbm(pressed);
            Intent i = new Intent(ListPrbmActivity.this, PrbmActivity.class);
            startActivity(i);
            finish();
        });
        whatToDoDialog.show();
    }
}