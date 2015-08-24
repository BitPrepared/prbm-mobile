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
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
 *
 * @author Nicola Corti
 */
public class UserData {

	/** Singleton instance */
	private static UserData instance = null;

    private static final String FILENAME = "prbms.bin";

	/** Reference to actual prbm */
	private Prbm prbm = null;
	/** Reference to actual unit */
	private PrbmUnit unit;
	/** Reference to actual entity */
	private PrbmEntity entity;
	/** Reference to actual entity column*/
	private int entityColumn;

	private List<Prbm> prbmList = null;
	private HashMap<Integer, Bitmap> backBitmaps;

	/**
	 * Empty constructor
	 */
	private UserData() {
		prbmList = new ArrayList<>();
	}

	/**
	 * Method used to return UserData instance
	 *
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
	 *
	 * @return Global reference to prbm
	 */
	public Prbm getPrbm() {
		return this.prbm;
	}

	/**
	 * Store actual prbm
	 *
	 * @param p Global reference to prbm
	 */
	public void setPrbm(Prbm p) {
		this.prbm = p;
	}

	/**
	 * Returns reference to actual entity
	 *
	 * @return Global reference to entity
	 */
	public PrbmEntity getEntity() {
		return this.entity;
	}

	public void setEntity(PrbmEntity entity) {
		this.entity = entity;
	}

	public PrbmUnit getUnit() {
		return unit;
	}

	public void setUnit(PrbmUnit unit) {
		this.unit = unit;
	}

	public int getColumn() {
		return entityColumn;
	}

	public void setColumn(int entityColumn) {
		this.entityColumn = entityColumn;
	}

	public void savePrbm(Context context, Prbm prbm){
		if (!this.prbmList.contains(prbm)) this.prbmList.add(prbm);
		saveAllPrbm(context);
	}
    public void savePrbm(Context context){
        savePrbm(context, this.prbm);
    }
	public void deletePrbm(Context context, Prbm prbm) {
		if (this.prbmList.contains(prbm)) this.prbmList.remove(prbm);
		saveAllPrbm(context);
	}
	public void deletePrbm(Context context) {
		deletePrbm(context, this.prbm);
	}

	public synchronized void saveAllPrbm(Context c){
		FileOutputStream fos;
		try {
			fos = c.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this.prbmList);
			if (oos != null) oos.close();
			if (fos != null) fos.close();

		} catch (FileNotFoundException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
	}

	public synchronized boolean restorePrbms(Context c){
		FileInputStream fis;
		try {
			fis = c.openFileInput(FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			this.prbmList = (ArrayList) ois.readObject();
			if (ois != null) ois.close();
			if (fis != null) fis.close();

		} catch (Exception e) { return false; }
		return true;
	}

	public List<Prbm> getAllPrbm() {
		return prbmList;
	}

	public void setBackBitmaps(HashMap<Integer, Bitmap> backBitmaps) {
		this.backBitmaps = backBitmaps;
	}

	public Bitmap getBackBitmap(int idListImage) {
		if (this.backBitmaps != null)
			return this.backBitmaps.get(idListImage);
		else
			return null;
	}

	public boolean canMoveUnitUp() {
		List<PrbmEntity> toCheck = unit.getEntitiesFromColumn(this.entityColumn);
		if (toCheck.indexOf(entity) > 0)
			return true;
		return false;
	}
	public boolean canMoveUnitDown() {
		List<PrbmEntity> toCheck = unit.getEntitiesFromColumn(this.entityColumn);
		if (toCheck.indexOf(entity) < toCheck.size()-1)
			return true;
		return false;
	}
}
