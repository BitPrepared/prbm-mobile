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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import it.bitprepared.prbm.mobile.R;

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
//                Intent login = new Intent(getApplicationContext(),
//                        ListPrbmActivity.class);
//                startActivity(login);
                break;
            case R.id.btnSyncro:
//                Intent sync = new Intent(getApplicationContext(),
//                        SyncroActivity.class);
//                startActivity(sync);
                break;
            case R.id.btnAbout:
                showAboutDialog();
                break;
            default:
                break;
        }
    }

    private void showAboutDialog() {

        ///////////////////////////////////////////////////////////////////////
        // CREAZIONE DELLE DIALOG BOX

        // Carico la view per il dialogbox e creo la dialogbox About
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.about_dialog, (ViewGroup) findViewById(R.id.layout_root));
        // Questa textview ha un compound drawable
        TextView text = (TextView) layout.findViewById(R.id.text);
        TextView web = (TextView) layout.findViewById(R.id.webtext);
        Linkify.addLinks(text, Linkify.ALL);
        Linkify.addLinks(web, Linkify.ALL);


        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(layout);

        builder.setPositiveButton(getString(R.string.contact), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"nicola@ncorti.it"});
                startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.sendmail)));
            }
        });
        builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton(getString(R.string.rate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=it.bitprepared.prbm.mobile")));
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}