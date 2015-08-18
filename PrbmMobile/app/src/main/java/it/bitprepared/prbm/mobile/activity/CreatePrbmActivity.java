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
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;

/**
 * Activity responsible for new PRBM Creation
 * @author Nicola Corti
 */
public class CreatePrbmActivity extends Activity {

    // Views references
    private EditText edtTitle, edtAuthors, edtPlace, edtNote;
    private DatePicker datDate;
    private TimePicker datTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting actionbar home button
        ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_prbm);

        // Inflating views
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtAuthors = (EditText) findViewById(R.id.edtAuthors);
        edtPlace = (EditText) findViewById(R.id.edtPlace);
        edtNote = (EditText) findViewById(R.id.edtNote);
        datDate = (DatePicker) findViewById(R.id.datDate);
        datTime = (TimePicker) findViewById(R.id.datTime);

        Calendar c = Calendar.getInstance(getResources().getConfiguration().locale);
        datDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datTime.setCurrentHour(c.get(Calendar.HOUR));
        datTime.setCurrentMinute(c.get(Calendar.MINUTE));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_prbm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.back || id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.confirm) {
            if (edtTitle.getText().length() == 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        new ContextThemeWrapper(this, R.style.AlertDialogCustom));
                alert.setTitle(R.string.fields_incomplete);
                alert.setMessage(getString(R.string.error_no_title_prbm));
                alert.setIcon(R.drawable.error);

                alert.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        });
                alert.show();
            } else {

                // Retrieving version name
                String version = "";
                try {
                    version = this.getPackageManager().getPackageInfo(
                            this.getPackageName(), 0).versionName;
                } catch (NameNotFoundException e) {
                    // Impossibile
                    e.printStackTrace();
                }

                // Creating a new PRBM
                Prbm newPrbm = new Prbm(version);
                newPrbm.setTitle(edtAuthors.getText().toString());
                newPrbm.setAuthors(edtAuthors.getText().toString());
                newPrbm.setPlace(edtPlace.getText().toString());
                newPrbm.setNote(edtNote.getText().toString());

                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", Locale.ITALY);
                Calendar c = Calendar.getInstance();
                c.set(datDate.getYear(), datDate.getMonth(), datDate.getDayOfMonth(), datTime.getCurrentHour(), datTime.getCurrentMinute());
                newPrbm.setDate(dateFormat.format(c));

                // Setting global PRBM
                UserData.getInstance().setPrbm(newPrbm);

//                Intent it = new Intent(CreatePrbmActivity.this,
//                        PrbmActivity.class);
//                startActivity(it);
//                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
