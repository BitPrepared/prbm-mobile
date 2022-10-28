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

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

/**
 * Activity responsible for main menu visualization
 */
public class MainActivity extends Activity implements OnClickListener {

    private final static int REQUEST_PERMISSIONS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_main);

        // Setting up button event listeners
        Button btnNew = findViewById(R.id.btnNew);
        Button btnList = findViewById(R.id.btnList);
        Button btnSync = findViewById(R.id.btnSyncro);
        Button btnAbout = findViewById(R.id.btnAbout);

        btnNew.setOnClickListener(this);
        btnList.setOnClickListener(this);
        btnSync.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // Handling button clicks
            case R.id.btnNew:
                checkIfPermissionsAreGranted();
                break;
            case R.id.btnList:
                Intent login = new Intent(getApplicationContext(),
                        ListPrbmActivity.class);
                startActivity(login);
                break;
            case R.id.btnSyncro:
                uploadPrbmJSONs();
                break;
            case R.id.btnAbout:
                showAboutDialog();
                break;
            default:
                break;
        }
    }

    private void checkIfPermissionsAreGranted() {
        List<String> permissionsToRequest = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.CAMERA);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!permissionsToRequest.isEmpty()) {
                requestPermissions(permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_CODE);
            } else {
                startNewPrbm();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            startNewPrbm();
        }
    }

    private void startNewPrbm() {
        Intent newprbm = new Intent(getApplicationContext(),
                    CreatePrbmActivity.class);
        startActivity(newprbm);
    }

    /**
     * Private method used for about dialog creation and visualization
     */
    private void showAboutDialog() {

        LayoutInflater inflater =
                (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout =
                inflater.inflate(R.layout.about_dialog, findViewById(R.id.layout_root));

        TextView text = layout.findViewById(R.id.text);
        TextView web = layout.findViewById(R.id.webtext);
        Linkify.addLinks(text, Linkify.ALL);
        Linkify.addLinks(web, Linkify.ALL);

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(layout);

        // Contact button
        builder
                .setPositiveButton(getString(R.string.contact), (dialog, which) -> {
                    final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL,
                            new String[]{"info@bitprepared.it"});
                    startActivity(Intent.createChooser(emailIntent, getResources()
                            .getString(R.string.sendmail)));
                });

        // Close button
        builder.setNegativeButton(getString(R.string.close), (dialog, which) -> dialog.dismiss());

        // Market button
        builder.setNeutralButton(getString(R.string.rate), (dialog, which) -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "market://details?id=it.bitprepared.prbm.mobile")));
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void uploadPrbmJSONs() {
        RemoteInterface remoteInterface = UserData.getInstance().getRestInterface();
        ProgressDialog barProgressDialog = new ProgressDialog(MainActivity.this);
        barProgressDialog.setTitle(getString(R.string.save_on_disk));
        barProgressDialog.setMessage(getString(R.string.saving_all_prbms));
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setIndeterminate(true);
        barProgressDialog.setCancelable(false);
        barProgressDialog.setCancelable(false);
        barProgressDialog.show();
        Gson gson = new GsonBuilder()
                .create();

        Disposable disposable = Observable.defer(() -> {
                    List<Prbm> list = UserData.getInstance().getAllPrbm();
                    for (Prbm prbm : list) {
                        String title = escape(prbm.getTitle());
                        String authors = escape(prbm.getAuthors());
                        String filename = title + "-" + authors + ".json";
                        String json = gson.toJson(prbm);
                        try {
                            remoteInterface.uploadPrbm(filename, json).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<PrbmEntity> entityList = new ArrayList<>();
                        for (PrbmUnit unit : prbm.getUnits()) {
                            entityList.addAll(unit.getFarLeft());
                            entityList.addAll(unit.getFarRight());
                            entityList.addAll(unit.getNearLeft());
                            entityList.addAll(unit.getNearRight());
                        }
                        for (PrbmEntity entity : entityList) {
                            if (!entity.getPictureName().isEmpty()) {
                                String picname = entity.getPictureName();
                                String picencoded = base64Encode(entity.getPictureURI());
                                try {
                                    remoteInterface.uploadImage(picname, picencoded).execute();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    return Observable.just(list);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> {
                    barProgressDialog.dismiss();
                    Toast.makeText(MainActivity.this, l.size() + " PRBM Sincronizzati", Toast.LENGTH_SHORT).show();
                }, t -> {
                    t.printStackTrace();
                    Toast.makeText(MainActivity.this, "Errore durante la Sincronizzazione!", Toast.LENGTH_SHORT).show();
                });
    }

    private String escape(String value) {
        return value.replaceAll("\\W+", "-");
    }

    private String base64Encode(Uri pictureURI) {
        Bitmap image;
        try {
            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pictureURI);
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOS);
            return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}