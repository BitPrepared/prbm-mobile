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
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;

/**
 * Activity responsible for new PRBM Creation
 */
public class CreatePrbmActivity extends AppCompatActivity {

  private EditText edtTitle;
  private EditText edtAuthors;
  private EditText edtPlace;
  private EditText edtNote;

  private EditText edtDate;
  private EditText edtTime;

  /**
   * Boolean flag for edit functionalities
   */
  private boolean edit = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Try to retrieve edit flag
    edit = getIntent().getBooleanExtra("edit", false);

    // Setting actionbar home button
    ActionBar bar = getActionBar();
    if (bar != null) bar.setDisplayHomeAsUpEnabled(true);
    setContentView(R.layout.activity_create_prbm);

    // Inflating views
    edtTitle = findViewById(R.id.edit_title);
    edtAuthors = findViewById(R.id.edit_authors);
    edtPlace = findViewById(R.id.edit_place);
    edtNote = findViewById(R.id.edit_notes);
    edtDate = findViewById(R.id.edit_date);
    edtTime = findViewById(R.id.edit_time);
    edtDate.setInputType(InputType.TYPE_NULL);
    edtTime.setInputType(InputType.TYPE_NULL);
    edtDate.setKeyListener(null);
    edtTime.setKeyListener(null);

    edtDate.setOnClickListener(view -> {
      MaterialDatePicker<Long> datePicker =
              MaterialDatePicker.Builder.datePicker()
                      .setTitleText(R.string.pick_prbm_date)
                      .setSelection(fromDateToTimestamp(edtDate.getText().toString()))
                      .build();
      datePicker.addOnPositiveButtonClickListener(view1 -> {
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(datePicker.getSelection()), ZoneId.of("UTC"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        edtDate.setText(time.format(dateFormatter));
      });
      datePicker.show(getSupportFragmentManager(), "datePicker");
    });

    edtTime.setOnClickListener(view -> {
      LocalTime time = LocalTime.parse(edtTime.getText().toString());
      MaterialTimePicker timePicker =
              new MaterialTimePicker.Builder()
                      .setTimeFormat(TimeFormat.CLOCK_24H)
                      .setHour(time.getHour())
                      .setMinute(time.getMinute())
                      .setTitleText(R.string.pick_prbm_time)
                      .build();
      timePicker.addOnPositiveButtonClickListener(view1 -> {
        LocalTime newTime = LocalTime.of(timePicker.getHour(), timePicker.getMinute());
        edtTime.setText(newTime.toString());
      });
      timePicker.show(getSupportFragmentManager(), "timePicker");
    });


    if (!edit) {
      // Setting actual date
      ZonedDateTime now = ZonedDateTime.now();
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
      edtDate.setText(now.toLocalDate().format(dateFormatter));
      edtTime.setText(now.toLocalTime().format(timeFormatter));
    } else {
      // Restoring date
      TextView txtTitle = findViewById(R.id.text_create_title);
      txtTitle.setText(getString(R.string.modify_prbm_parameters));

      Prbm thisPrbm = UserData.getPrbm();
      edtTitle.setText(thisPrbm.getTitle());
      edtAuthors.setText(thisPrbm.getAuthors());
      edtPlace.setText(thisPrbm.getPlace());
      edtNote.setText(thisPrbm.getNote());
      ZonedDateTime date = ZonedDateTime.parse(thisPrbm.getDate());
      edtDate.setText(date.toLocalDate().toString());
      edtTime.setText(date.toLocalTime().toString());
    }
  }

  @Override
  public boolean onCreateOptionsMenu(@NonNull Menu menu) {
    if (!edit)
      getMenuInflater().inflate(R.menu.create_prbm, menu);
    else
      getMenuInflater().inflate(R.menu.create_prbm_edit_param, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.back || id == android.R.id.home) {
      finish();
      return true;
    } else if (id == R.id.confirm) {

      // Checking if Title is not empty
      if (edtTitle.getText().length() == 0) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.fields_incomplete);
        alert.setMessage(getString(R.string.error_no_title_prbm));
        alert.setIcon(R.drawable.ic_alert_black_48dp);
        alert.setPositiveButton(R.string.ok,
                (dialog, whichButton) -> dialog.dismiss());
        alert.show();
      } else {
        // Checking if PRBM must be created or not
        Prbm thisPrbm;
        if (!edit) {
          thisPrbm = new Prbm();
        } else {
          thisPrbm = UserData.getPrbm();
        }

        thisPrbm.setTitle(edtTitle.getText().toString());
        thisPrbm.setAuthors(edtAuthors.getText().toString());
        thisPrbm.setPlace(edtPlace.getText().toString());
        thisPrbm.setNote(edtNote.getText().toString());

        LocalDate localDate = LocalDate.parse(edtDate.getText().toString());
        LocalTime localTime = LocalTime.parse(edtTime.getText().toString());
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault());
        thisPrbm.setDate(zonedDateTime.toString());

        if (!edit) {
          thisPrbm.addNewUnits();
          // Setting global PRBM
          UserData.setPrbm(thisPrbm);
          Intent it = new Intent(CreatePrbmActivity.this, PrbmActivity.class);
          startActivity(it);
        }
        finish();
      }
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private Long fromDateToTimestamp(String date) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate ldate = LocalDate.parse(date, dateFormatter);
    return ldate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli();
  }
}