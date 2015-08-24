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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

/**
 * Class responsible for visualizing and modifying a single PrbmEntity
 * @author Nicola Corti
 */
public class EntityActivity extends Activity {

    /** Debug TAG */
    private final static String TAG = "EntityActivity";

    private LinearLayout linFree = null;

    private EditText edtCaption = null;
    private EditText edtDescription = null;
    private TimePicker datTime = null;

    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        edit = intent.getBooleanExtra("edit", false);

        // Setting up Home back button
        ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_entity);
        linFree = (LinearLayout) findViewById(R.id.linearFreeEntity);
        ImageView imgBack = (ImageView) findViewById(R.id.imgEntity);
        TextView txtTitle = (TextView) findViewById(R.id.txtEntityTitleAdd);
        datTime = (TimePicker) findViewById(R.id.datTimeEntity);
        edtCaption = (EditText) findViewById(R.id.edtCaption);
        edtDescription = (EditText) findViewById(R.id.edtDescription);

        Calendar c = Calendar.getInstance(getResources().getConfiguration().locale);
        datTime.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        datTime.setCurrentMinute(c.get(Calendar.MINUTE));
        datTime.setIs24HourView(true);

        PrbmEntity entity = UserData.getInstance().getEntity();
        if (entity != null) {
            imgBack.setImageResource(entity.getIdBackImage());
            entity.drawYourSelf(EntityActivity.this, linFree);
            txtTitle.setText(entity.getType());

            if (edit) {
                edtCaption.setText(entity.getCaption());
                edtDescription.setText(entity.getDescription());
                entity.restoreFields(this, linFree);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.prbm_entity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.discard || id == android.R.id.home) {

            AlertDialog.Builder build = new AlertDialog.Builder(EntityActivity.this);
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
        } else if (id == R.id.save) {
            if (edtCaption.getText().length() == 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        new ContextThemeWrapper(this, R.style.AlertDialogCustom));
                alert.setTitle(R.string.fields_incomplete);
                alert.setMessage(getString(R.string.you_must_insert_caption));
                alert.setIcon(R.drawable.ic_alert_black_48dp);
                alert.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        });
                alert.show();
            } else {
                PrbmEntity entity = UserData.getInstance().getEntity();
                if (entity != null) {
                    entity.saveFields(EntityActivity.this, linFree);
                    entity.setCaption(edtCaption.getText().toString());
                    entity.setDescription(edtDescription.getText().toString());
                    if (!edit) {
                        PrbmUnit involved = UserData.getInstance().getUnit();
                        involved.addEntity(entity, UserData.getInstance().getColumn());
                    }
                    setResult(RESULT_OK);
                    if (PrbmAddEntityActivity.self != null)
                        PrbmAddEntityActivity.self.finish();
                    finish();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
