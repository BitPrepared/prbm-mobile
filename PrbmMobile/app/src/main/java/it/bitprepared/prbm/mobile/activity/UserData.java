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

import android.content.Context;
import android.graphics.Bitmap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.bitprepared.prbm.mobile.model.Prbm;
import it.bitprepared.prbm.mobile.model.PrbmEntity;
import it.bitprepared.prbm.mobile.model.PrbmUnit;

/**
 * Class used to collect global data related to user/application
 * Realized with singleton pattern
 * @author Nicola Corti
 */
public class UserData {

    /** Singleton instance */
    private static UserData instance = null;

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
    private static final String FILENAME = "prbms.bin";

    /** HashMap of bitmpas (for fast access) */
    private HashMap<Integer, Bitmap> backBitmaps;

    /**
     * Empty constructor
     */
    private UserData() {
        prbmList = new ArrayList<>();
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
    private synchronized void saveAllPrbm(Context c) {
        FileOutputStream fos;
        try {
            fos = c.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.prbmList);
            oos.close();
            if (fos != null) fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserialize all prbms from private file
     * @param c Execution context
     */
    private synchronized boolean restorePrbms(Context c) {
        FileInputStream fis;
        try {
            fis = c.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.prbmList = (ArrayList) ois.readObject();
            ois.close();
            if (fis != null) fis.close();

        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
