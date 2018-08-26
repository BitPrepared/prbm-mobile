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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class responsible for visualizing and modifying a single PrbmEntity
 */
public class EntityActivity extends Activity {

    /** Debug TAG */
    private final static String TAG = "EntityActivity";
    private static final int CAMERA_RESULT = 1005;
    private static final int GALLERY_RESULT = 1006;

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
    private transient Uri capturedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        edit = intent.getBooleanExtra("edit", false);

        // Setting up Home back button
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        // Inflating views
        setContentView(R.layout.activity_entity);
        linFree = findViewById(R.id.linearFreeEntity);
        ImageView imgBack = findViewById(R.id.imgEntity);
        TextView txtTitle = findViewById(R.id.txtEntityTitleAdd);
        datTime = findViewById(R.id.datTimeEntity);
        edtCaption = findViewById(R.id.edtCaption);
        edtDescription = findViewById(R.id.edtDescription);
        imgCamera = findViewById(R.id.imgCamera);

        PrbmEntity entity = UserData.getInstance().getEntity();
        if (entity != null) {
            imgBack.setImageResource(entity.getIdBackImage());
            entity.drawYourSelf(EntityActivity.this, linFree);
            txtTitle.setText(entity.getType());

            Gson gson = new Gson();
            Log.e("TAG", gson.toJson(entity));

            if (!edit) {
                // Setting current hour
                Calendar c = Calendar.getInstance(getResources().getConfiguration().locale);
                datTime.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
                datTime.setCurrentMinute(c.get(Calendar.MINUTE));
                datTime.setIs24HourView(true);
            } else {
                edtCaption.setText(entity.getCaption());
                edtDescription.setText(entity.getDescription());
                if (!entity.getPictureName().isEmpty()) {
                    capturedImageUri = entity.getPictureURI();
                    imgCamera.setVisibility(View.VISIBLE);

                    Glide
                            .with(this)
                            .load(capturedImageUri)
                            .apply(new RequestOptions().override(600, 300).centerCrop())
                            .into(imgCamera);
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
            build.setPositiveButton(R.string.ok, (dialog, which) -> {
                PrbmEntity entity = UserData.getInstance().getEntity();
                entity.setPictureName("");
                finish();
            });
            build.setNegativeButton(R.string.abort, (dialog, which) -> {
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
                        (dialog, whichButton) -> dialog.dismiss());
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
                    setResult(RESULT_OK);
                    if (PrbmAddEntityActivity.self != null) {
                        PrbmAddEntityActivity.self.finish();
                    }
                    finish();
                }
            }
            return true;
        } else if (id == R.id.pic) {
            PrbmEntity entity = UserData.getInstance().getEntity();
            CharSequence[] itemadd = {"Scatta una foto",
                                      "Scegli dalla Galleria"};
            CharSequence[] itemadddelete = {"Scatta una foto",
                                            "Scegli dalla Galleria", "Rimuovi foto"};
            boolean deleteflag = (entity != null && !entity.getPictureName().isEmpty());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.ic_camera_iris_black_36dp);
            builder.setTitle("Fotografia");
            builder.setItems((deleteflag ? itemadddelete : itemadd),
                             (dialog, which) -> {
                                 switch (which) {
                                     case 0:
                                         takePicture();
                                         dialog.dismiss();
                                         break;
                                     case 1:
                                         chooseFromGallery();
                                         dialog.dismiss();
                                         break;
                                     case 2:
                                         entity.setPictureName("");
                                         imgCamera.setVisibility(View.GONE);
                                         dialog.dismiss();
                                         break;
                                     default:
                                         break;
                                 }
                             });
            AlertDialog alert = builder.create();
            alert.setCancelable(true);
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private String getFilenameFromURI(Uri uri) {
        if (uri == null) {
            return "";
        }
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

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_RESULT);
    }

    private void takePicture() {

        Calendar cal = Calendar.getInstance();
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/PRBM");
        dir.mkdirs();
        String picname = cal.getTimeInMillis() + ".jpg";
        File destfile = new File(dir, (cal.getTimeInMillis() + ".jpg"));

        if (!destfile.exists()) {
            try {
                destfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            destfile.delete();
            try {
                destfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        capturedImageUri = Uri.fromFile(destfile);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        startActivityForResult(i, CAMERA_RESULT);
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PrbmEntity entity = UserData.getInstance().getEntity();

        if (requestCode == CAMERA_RESULT) {
            imgCamera.setVisibility(View.VISIBLE);
            Glide
                    .with(this)
                    .load(capturedImageUri)
                    .apply(new RequestOptions().override(600, 300).centerCrop())
                    .into(imgCamera);

            entity.setPictureName(getFilenameFromURI(capturedImageUri));
            Log.d(TAG, "capturedImage " + capturedImageUri.getPath());
        } else if (requestCode == GALLERY_RESULT) {
            if (resultCode != RESULT_OK) {
                return;
            }
            if (data == null) {
                Toast.makeText(this, "Impossibile caricare l'immagine", Toast.LENGTH_SHORT).show();
                return;
            }
            capturedImageUri = data.getData();
            imgCamera.setVisibility(View.VISIBLE);
            Glide
                    .with(this)
                    .load(data.getData())
                    .apply(new RequestOptions().override(600, 300).centerCrop())
                    .into(imgCamera);

            Calendar cal = Calendar.getInstance();
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/PRBM");
            dir.mkdirs();
            String picname = cal.getTimeInMillis() + ".jpg";
            File destfile = new File(dir, (cal.getTimeInMillis() + ".jpg"));

            if (!destfile.exists()) {
                try {
                    destfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                destfile.delete();
                try {
                    destfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                copy(new File(getRealPathFromUri(this, capturedImageUri)), destfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            entity.setPictureName(picname);
        }
    }
}
