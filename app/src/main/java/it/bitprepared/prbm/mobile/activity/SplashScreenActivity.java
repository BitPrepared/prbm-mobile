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
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import it.bitprepared.prbm.mobile.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Activity responsible for showing an initial splash screen.
 * @author Nicola Corti
 */
public class SplashScreenActivity extends Activity {

    /** Debug TAG */
    private final static String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Locking orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_splash);

        // Showing version number
        TextView vers = (TextView) findViewById(R.id.versionNameSplash);
        try {
            vers.setText(this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName);
        } catch (NameNotFoundException e) {
            // Impossible
            e.printStackTrace();
        }

        new LoadTask(SplashScreenActivity.this).execute();
    }


    /**
     * Async task used to
     * - Restore PRBM from serialized files
     * - Pre-fetch big bitmaps, to enhance performance
     * @author Nicola Corti
     */
    public class LoadTask extends AsyncTask<Void, Void, Void> {

        /** Reference to Activity */
        private Activity myActivity;

        /** Hashmap of Integers (IDs) and Bitmaps */
        private HashMap<Integer, Bitmap> bitmaps;
        /** List of Resource ID (bitmaps) to be loaded */
        private List<Integer> ids;

        /**
         * Basic constructor, mantains a reference to the activity
         * @param me Activity that launched this AsyncTask
         */
        public LoadTask(Activity me) {
            this.myActivity = me;
            this.bitmaps = new HashMap<>();
            this.ids = new ArrayList<>();
            // List of IDs to be loaded */
            this.ids.add(R.drawable.background_curiosity_list);
            this.ids.add(R.drawable.background_fauna_list);
            this.ids.add(R.drawable.background_flower_list);
            this.ids.add(R.drawable.background_forecast_list);
            this.ids.add(R.drawable.background_interview_list);
            this.ids.add(R.drawable.background_monument_list);
            this.ids.add(R.drawable.background_news_list);
            this.ids.add(R.drawable.background_panorama_list);
            this.ids.add(R.drawable.background_tree_list);
            this.ids.add(R.drawable.background_other_list);
            this.ids.add(R.drawable.background_building_list);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Restore Serialized PRBMs
            UserData.getInstance().restorePrbms(SplashScreenActivity.this);

            // Load Bitmaps
            for (Integer id : ids) {
                InputStream is = myActivity.getResources().openRawResource(id);
                bitmaps.put(id, BitmapFactory.decodeStream(is));
            }
            UserData.getInstance().setBackBitmaps(bitmaps);
            return null;
        }

        @Override
        protected void onPostExecute(Void ret) {
            Intent main;
            // Launch main activity
            main = new Intent(myActivity, MainActivity.class);
            myActivity.startActivity(main);
            myActivity.finish();
        }

    }
}