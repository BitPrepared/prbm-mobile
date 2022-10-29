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
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.graphics.Bitmap;

import it.bitprepared.prbm.mobile.model.Prbm;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class used to collect global data related to user/application
 * Realized with singleton pattern
 */
public class UserData {

    /** Singleton instance */
    private static UserData instance = null;

    /** Base URL for HTTP Calls */
    private static final String API_BASE_URL = "http://prbm.bitprepared.it/";

    /** Reference to actual prbm */
    private Prbm prbm = null;

    /** Reference to actual entity */
    private PrbmEntity entity;
    /** Reference to actual entity column */
    private int entityColumn;
    /** List of PRBMS */
    private List<Prbm> prbmList = null;

    /** Reference to actual unit */
    private PrbmUnit unit;
    /** Serilize filename */
    private static final String FILENAME = "prbms.json";

    /** HashMap of bitmpas (for fast access) */
    private HashMap<Integer, Bitmap> backBitmaps;

    /** Reference to remote Retrofit instance */
    private RemoteInterface restInterface;

    /**
     * Empty constructor
     */
    private UserData() {
        prbmList = new ArrayList<>();
        Gson gson = new GsonBuilder()
            .setLenient()
            .create();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        restInterface = retrofit.create(RemoteInterface.class);
    }

    /**
     * Method used to return UserData instance
     * @return Reference to singleton instance
     */
    public static synchronized UserData getInstance() {
        if (null == instance) {
            instance = new UserData();
        }
        return instance;
    }

    /**
     * Returns reference to actual prbm
     * @return Global reference to prbm
     */
    public Prbm getPrbm() {
        return this.prbm;
    }

    /**
     * Store actual prbm
     * @param p Global reference to prbm
     */
    public void setPrbm(Prbm p) {
        this.prbm = p;
    }

    /**
     * Returns reference to actual unit
     * @return Global reference to a Prbm Unit
     */
    public PrbmUnit getUnit() {
        return unit;
    }

    /**
     * Store actual unit
     * @param unit Global reference to a Prbm Unit
     */
    public void setUnit(PrbmUnit unit) {
        this.unit = unit;
    }

    /**
     * Returns reference to actual entity
     * @return Global reference to actual entity
     */
    public PrbmEntity getEntity() {
        return this.entity;
    }

    /**
     * Store actual entity
     * @param entity Global reference to a Prbm Entity
     */
    public void setEntity(PrbmEntity entity) {
        this.entity = entity;
    }

    /**
     * Returns reference to actual entity column (1-4)
     * @return Global reference to actual entity column
     */
    public int getColumn() {
        return entityColumn;
    }

    /**
     * Store actual entity column (1-4)
     * @param entityColumn Global reference to an entity column
     */
    public void setColumn(int entityColumn) {
        this.entityColumn = entityColumn;
    }

    /**
     * Check if actual unit can be moved up in its column
     * @return True if actual unit can be moved up
     */
    public boolean canMoveUnitUp() {
        List<PrbmEntity> toCheck = unit.getEntitiesFromColumn(this.entityColumn);
        if (toCheck.indexOf(entity) > 0)
            return true;
        return false;
    }

    /**
     * Check if actual unit can be moved down in its column
     * @return True if actual unit can be moved down
     */
    public boolean canMoveUnitDown() {
        List<PrbmEntity> toCheck = unit.getEntitiesFromColumn(this.entityColumn);
        if (toCheck.indexOf(entity) < toCheck.size() - 1)
            return true;
        return false;
    }

    /**
     * Store a PRBM and serialize the PRBM list
     * @param context Execution context
     * @param prbm    Prbm to be saved
     */
    public void savePrbm(Context context, Prbm prbm) {
        if (!this.prbmList.contains(prbm)) this.prbmList.add(prbm);
        saveAllPrbm(context);
    }

    /**
     * Store actual PRBM and serialize the PRBM list
     * @param context Execution context
     */
    public void savePrbm(Context context) {
        savePrbm(context, this.prbm);
    }

    /**
     * Delete a PRBM and serialize the PRBM list
     * @param context Execution context
     * @param prbm    Prbm to be deleted
     */
    public void deletePrbm(Context context, Prbm prbm) {
        if (this.prbmList.contains(prbm)) this.prbmList.remove(prbm);
        saveAllPrbm(context);
    }

    /**
     * Delete actual PRBM and serialize the PRBM list
     * @param context Execution context
     */
    public void deletePrbm(Context context) {
        deletePrbm(context, this.prbm);
    }

    /**
     * Returns list of all Prbms stored
     * @return A list of Prbms currently stored
     */
    public List<Prbm> getAllPrbm() {
        return prbmList;
    }

    /**
     * Set a loaded hashmap of bitmaps. The hashmap can be used later to improve
     * performances while retrieving bitmaps.
     * @param backBitmaps An hash map of Integer (IDs) and loaded Bitmaps
     */
    public void setBackBitmaps(HashMap<Integer, Bitmap> backBitmaps) {
        this.backBitmaps = backBitmaps;
    }

    /**
     * Return a loaded bitmaps from a Resource ID
     * @param idListImage Resource ID of requested bitmap
     * @return Already loeaded bitmap
     */
    public Bitmap getBackBitmap(int idListImage) {
        if (this.backBitmaps != null)
            return this.backBitmaps.get(idListImage);
        else
            return null;
    }

    /**
     * Serialize all prbms to private file
     * @param c Execution context
     */
    protected synchronized void saveAllPrbm(Context c) {
        FileOutputStream fos;
        try {
            Gson g = new Gson();
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File (root.getAbsolutePath() + "/PRBM");
            dir.mkdirs();
            String json;
            for (Prbm p : this.prbmList){
                json = g.toJson(p);
                File file = new File(dir, p.getTitle() + "-" + p.getAuthors() + "-" + p.getDate() + ".json");
                fos = new FileOutputStream(file);
                fos.write(json.getBytes());
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserialize all prbms from private file
     * @param c Execution context
     */
    protected synchronized boolean restorePrbms(Context c) {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/PRBM");
        String[] prbms = dir.list();
        if (prbms == null || prbms.length == 0){
            // Nothing to restore
            return true;
        }
        this.prbmList = new ArrayList<>();
        for (String file : prbms){
            if (!file.endsWith(".json")) continue;
            FileInputStream fis;
            try {
                fis = new FileInputStream(new File(dir, file));
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                String json = sb.toString();
                Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PrbmEntity.class, new PrbmEntityDecoder())
                    .create();
                Type prbmType = new TypeToken<Prbm>(){}.getType();
                Prbm decoded = gson.fromJson(json, prbmType);
                this.prbmList.add(decoded);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public RemoteInterface getRestInterface() {
        return restInterface;
    }
}
