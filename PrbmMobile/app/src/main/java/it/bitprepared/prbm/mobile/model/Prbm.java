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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class Prbm {

    /** DB ID of Prbm */
    private long id = -1;

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
     * Setter if Database ID.
     * @param id Prbm database ID
     */
    public void setID(long id) {
        this.id = id;
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
            jsonObject.put("id", id);
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<PrbmUnit> getUnits(){ return units; }

    public void addNewUnits(){
        units.add(new PrbmUnit());
    }
    public void addNewUnits(int posit, boolean after){
        if (after)
            units.add(posit+1, new PrbmUnit());
        else
            units.add(posit, new PrbmUnit());
    }
    public void addNewUnits(PrbmUnit unit, boolean after){
        int posit = units.indexOf(unit);
        addNewUnits(posit, after);
    }

    public boolean canDelete() {
        return units.size() > 1;
    }

    public void deleteUnit(PrbmUnit u){
        units.remove(u);
    }
    public void deleteUnit(int pos){
        units.remove(pos);
    }
}