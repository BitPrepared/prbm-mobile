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

package it.bitprepared.prbm.mobile.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class related to model. It represent a single Prbm, stored in local DB, composed
 * by several PrbmUnit.
 * @author Nicola Corti
 */
public class Prbm implements Serializable {

    /** Debug TAG */
    private final static String TAG = "Prbm";

    /** Creation timestamp of Prbm */
    private String time;
    /** App version */
    private String version;

    /** Title of Prbm */
    private String title = null;
    /** Authors of Prbm */
    private String authors = null;
    /** Notes related to Prbm */
    private String note = null;
    /** Date of Prbm (different from timestamp) */
    private String date = null;
    /** Place of Prbm */
    private String place = null;

    //private int sync = 0;

    /** List of Units */
    private List<PrbmUnit> units;

    // //////////////////////////////////////
    //
    // CONSTRUCTOR
    //
    // //////////////////////////////////////

    /**
     * Base constructor for a new Prbm
     * @param version App version name
     * @param time    Timestamp of creation
     */
    public Prbm(String version, String time) {
        this.time = time;
        this.version = version;
        this.units = new ArrayList<>();
    }

    /**
     * Base constructor for a new Prbm. It sets timestamp to actual timestamp.
     * @param version App version name
     */
    public Prbm(String version) {
        this(version, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.ITALY);
        this.time = dateFormat.format(new Date());
    }

    /**
     * Return JSON representation of a single Prbm
     * @return A JSONObject from a Prbm
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            // Primitive fields
            jsonObject.put("time", time);
            jsonObject.put("version", version);

            jsonObject.put("note", note);

            // List of units
            JSONArray jsonArray = new JSONArray();
            for (PrbmUnit u : units) {
                jsonArray.put(u.toJSONObject());
            }
            jsonObject.put("units", jsonArray);

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Static - Returns a JSON representation from a List of Prbms
     * @param list A list of Prbm
     * @return JSON Representation
     */
    public static String toJSONArray(ArrayList<Prbm> list) {

        // Creating JSON Array
        JSONArray jsonArray = new JSONArray();
        for (Prbm s : list) {
            jsonArray.put(s.toJSONObject());
        }

        // Inserting into global 'prbms' objects
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("prbms", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * Getter for PRBM Title
     * @return Prbm Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for PRBM Title
     * @param title The Prbm Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for PRBM Authors
     * @return Prbm Authors
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * Setter for PRBM Authors
     * @param authors The Prbm Authors
     */
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    /**
     * Getter for PRBM Date (timestamp)
     * @return Prbm Date
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for PRBM Date
     * @param date The Prbm Date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for PRBM Place
     * @return Prbm Place
     */
    public String getPlace() {
        return place;
    }

    /**
     * Setter for PRBM Place
     * @param place The Prbm Place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * Getter for PRBM Notes
     * @return Prbm Notes
     */
    public String getNote() {
        return note;
    }

    /**
     * Setter for PRBM Note
     * @param note The Prbm Notes
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Getter for PRBM Unit list
     * @return PrbmUnit list
     */
    public List<PrbmUnit> getUnits() {
        return units;
    }

    /**
     * Getter for a single PRBM Unit
     * @param pos Position of PRBM unit
     * @return Unit at specified position
     */
    public PrbmUnit getUnit(int pos) {
        return units.get(pos);
    }

    /**
     * Public method to add a new empty unit at the end of unit list
     */
    public void addNewUnits() {
        units.add(new PrbmUnit());
    }

    /**
     * Public method to add a new empty unit at a specific position.
     * Units will be shifted subsequently.
     * @param posit Position of new Unit.
     * @param after Boolean flag to set if new unit must be inserted before or after posit
     */
    public void addNewUnits(int posit, boolean after) {
        if (after)
            units.add(posit + 1, new PrbmUnit());
        else
            units.add(posit, new PrbmUnit());
    }

    /**
     * Public method to add a new empty unit after a specific unit.
     * Units will be shifted subsequently.
     * @param unit Unit involved in adding.
     * @param after Boolean flag to set if new unit must be inserted before or after posit
     */
    public void addNewUnits(PrbmUnit unit, boolean after) {
        int posit = units.indexOf(unit);
        addNewUnits(posit, after);
    }

    /**
     * Public method to check if Unit can be deleted. At least one unit must be present
     * @return True if Unit can be deleted, false otherwise.
     */
    public boolean canDelete() {
        return units.size() > 1;
    }

    /**
     * Delete a single Prbm Unit
     * @param u Unit to be deleted
     */
    public void deleteUnit(PrbmUnit u) {
        units.remove(u);
    }

    /**
     * Delete PRBM unit in a specific position
     * @param pos Position of Unit to be deleted
     */
    public void deleteUnit(int pos) {
        units.remove(pos);
    }

    /**
     * Public method to print a DUBUG dump of PRBM
     */
    public void print() {
        Log.d(TAG, "--- Printing PRBM ---");
        for (int i = 0; i < units.size(); i++) {
            PrbmUnit u = units.get(i);
            Log.d(TAG, " --- Unit " + i);
            for (int j = 0; j < u.getFarLeft().size(); j++) {
                PrbmEntity e = u.getFarLeft().get(j);
                Log.d(TAG, " ------ Entity " + j + " Tyep " + e.getType());
            }
            for (int j = 0; j < u.getNearLeft().size(); j++) {
                PrbmEntity e = u.getNearLeft().get(j);
                Log.d(TAG, " ------ Entity " + j + " Tyep " + e.getType());
            }
            for (int j = 0; j < u.getNearRight().size(); j++) {
                PrbmEntity e = u.getNearRight().get(j);
                Log.d(TAG, " ------ Entity " + j + " Tyep " + e.getType());
            }
            for (int j = 0; j < u.getFarRight().size(); j++) {
                PrbmEntity e = u.getFarRight().get(j);
                Log.d(TAG, " ------ Entity " + j + " Tyep " + e.getType());
            }
        }
    }
}