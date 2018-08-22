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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.util.Linkify;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import it.bitprepared.prbm.mobile.R;
import it.bitprepared.prbm.mobile.model.Prbm;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Activity responsible for main menu visualization
 * @author Nicola Corti
 */
public class MainActivity extends Activity implements OnClickListener {

    /** Dubug TAG */
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_main);

        // Setting up button event listeners
        Button btnNew = (Button) findViewById(R.id.btnNew);
        Button btnList = (Button) findViewById(R.id.btnList);
        Button btnSync = (Button) findViewById(R.id.btnSyncro);
        Button btnAbout = (Button) findViewById(R.id.btnAbout);

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
                Intent newprbm = new Intent(getApplicationContext(),
                                            CreatePrbmActivity.class);
                startActivity(newprbm);
                break;
            case R.id.btnList:
                Intent login = new Intent(getApplicationContext(),
                                          ListPrbmActivity.class);
                startActivity(login);
                break;
            case R.id.btnSyncro:
                SimpleDateFormat
                        sdf =
                        new SimpleDateFormat("yyyyMMdd_HHmmss",
                                getResources().getConfiguration().locale);
                String currentDateandTime = sdf.format(new Date());
                uploadPrbmJSONs(currentDateandTime);
                break;
            case R.id.btnAbout:
                showAboutDialog();
                break;
            default:
                break;
        }
    }

    /**
     * Private method used for about dialog creation and visualization
     */
    private void showAboutDialog() {

        LayoutInflater
            inflater =
            (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View
            layout =
            inflater.inflate(R.layout.about_dialog, (ViewGroup) findViewById(R.id.layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        TextView web = (TextView) layout.findViewById(R.id.webtext);
        Linkify.addLinks(text, Linkify.ALL);
        Linkify.addLinks(web, Linkify.ALL);

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(layout);

        // Contact button
        builder
            .setPositiveButton(getString(R.string.contact), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                         new String[]{"info@bitprepared.it"});
                    startActivity(Intent.createChooser(emailIntent, getResources()
                        .getString(R.string.sendmail)));
                }
            });

        // Close button
        builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Market button
        builder.setNeutralButton(getString(R.string.rate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "market://details?id=it.bitprepared.prbm.mobile")));
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void uploadPrbmJSONs(final String prefix) {
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

        Observable.defer(() -> {
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
                List<PrbmEntity> entityList = new ArrayList<PrbmEntity>();
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
//                        Log.d("TAG", "ENCODED");
//                        Log.d("TAG", picencoded);
                        try {
                            remoteInterface.uploadImage(picname, picencoded).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return Observable.just(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pictureURI);
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOS);
            String encoded = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
//            Log.e("TAG", "Encoded: " + encoded);
            return encoded;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}