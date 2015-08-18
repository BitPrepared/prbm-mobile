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
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import it.bitprepared.prbm.mobile.R;

/**
 * Activity responsible for showing an initial splash screen.
 * @author Nicola Corti
 */
public class SplashScreenActivity extends Activity {

    /** Debug TAG */
    private final static String TAG = "SplashScreen";

    /** Splash screen timer */
    private final static int SPLASH_TIMER = 2000;

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

        // Goto to MainActivity after SPLASH_TIMER seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(main);
                finish();
            }
        }, SPLASH_TIMER);

    }
}