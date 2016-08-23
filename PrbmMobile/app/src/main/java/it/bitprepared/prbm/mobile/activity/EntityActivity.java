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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class responsible for visualizing and modifying a single PrbmEntity
 * @author Nicola Corti
 */
public class EntityActivity extends Activity {

    /** Debug TAG */
    private final static String TAG = "EntityActivity";
    private static final int CAMERA_RESULT = 1005;

    /** Linear layout left free to each Entity */
    private LinearLayout linFree = null;

    /** Reference to Entity Caption textbox */
    private EditText edtCaption = null;
    /** Reference to Entity Description textbox */
    private EditText edtDescription = null;
    /** Reference to Entity Time textbox */
    private TimePicker datTime = null;
    /** Reference to the ImageView */
    private ImageView imgCamera = null;

    /** Boolean flag for edit functionalities */
    private boolean edit = false;

    /** Image URI */
    private static Uri capturedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        edit = intent.getBooleanExtra("edit", false);

        // Setting up Home back button
        ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        // Inflating views
        setContentView(R.layout.activity_entity);
        linFree = (LinearLayout) findViewById(R.id.linearFreeEntity);
        ImageView imgBack = (ImageView) findViewById(R.id.imgEntity);
        TextView txtTitle = (TextView) findViewById(R.id.txtEntityTitleAdd);
        datTime = (TimePicker) findViewById(R.id.datTimeEntity);
        edtCaption = (EditText) findViewById(R.id.edtCaption);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
        imgCamera = (ImageView) findViewById(R.id.imgCamera);

        PrbmEntity entity = UserData.getInstance().getEntity();
        if (entity != null) {
            imgBack.setImageResource(entity.getIdBackImage());
            entity.drawYourSelf(EntityActivity.this, linFree);
            txtTitle.setText(entity.getType());

            if (!edit) {
                // Setting current hour
                Calendar c = Calendar.getInstance(getResources().getConfiguration().locale);
                datTime.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
                datTime.setCurrentMinute(c.get(Calendar.MINUTE));
                datTime.setIs24HourView(true);
            } else {
                edtCaption.setText(entity.getCaption());
                edtDescription.setText(entity.getDescription());

                if (!entity.getPictureName().isEmpty()){
                    capturedImageUri = entity.getPictureURI();
                    imgCamera.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(capturedImageUri).resize(600,300).centerInside().into(imgCamera);
                }

                // Restoring timestamp to TimePicker
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", getResources().getConfiguration().locale);
                try {
                    Date date = dateFormat.parse(entity.getTimestamp());
                    Calendar cal = Calendar.getInstance(getResources().getConfiguration().locale);
                    cal.setTime(date);
                    datTime.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
                    datTime.setCurrentMinute(cal.get(Calendar.MINUTE));
                } catch (ParseException e) {
                }
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

            // Checking if caption is empty
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
                    Calendar c = Calendar.getInstance(getResources().getConfiguration().locale);

                    // Saving timestamp. Date set at Unix epoch
                    c.set(1970, 1, 1, datTime.getCurrentHour(), datTime.getCurrentMinute());
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss", getResources().getConfiguration().locale);
                    entity.setTimestamp(dateFormat.format(c.getTime()));

                    if (!edit) {
                        PrbmUnit involved = UserData.getInstance().getUnit();
                        involved.addEntity(entity, UserData.getInstance().getColumn());
                    }
                    entity.setPictureName(getFilenameFromURI(capturedImageUri));
                    setResult(RESULT_OK);
                    if (PrbmAddEntityActivity.self != null)
                        PrbmAddEntityActivity.self.finish();
                    finish();
                }
            }
            return true;
        } else if (id == R.id.pic) {
            PrbmEntity entity = UserData.getInstance().getEntity();
            if (entity != null && !entity.getPictureName().isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Modificare la foto?");
                alert.setMessage("È già presente una foto, cosa vuoi fare?");
                alert.setNegativeButton("Cestinala", (dialog, which) -> {
                    entity.setPictureName("");
                    imgCamera.setVisibility(View.GONE);
                });
                alert.setNeutralButton("Annulla", (dialog, which) -> dialog.dismiss());
                alert.setPositiveButton("Riscatta", (dialog, which) -> takePicture());
                alert.show();
            } else {
                takePicture();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String getFilenameFromURI(Uri uri) {
        if (uri == null)
            return "";
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void takePicture() {
        Calendar cal = Calendar.getInstance();

        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/PRBM");
        dir.mkdirs();
        File file = new File(dir, (cal.getTimeInMillis() + ".jpg"));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        capturedImageUri = Uri.fromFile(file);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        startActivityForResult(i, CAMERA_RESULT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RESULT) {
            imgCamera.setVisibility(View.VISIBLE);
            Picasso.with(this).load(capturedImageUri).resize(600,300).centerInside().into(imgCamera);
        }
    }
}
