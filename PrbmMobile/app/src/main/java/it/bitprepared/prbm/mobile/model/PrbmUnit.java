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

import java.util.ArrayList;
import java.util.List;

/**
 * Class related to model. PrbmUnit represent a single row of
 * Prbm that is composed by several PrbmEntity
 * @author Nicola Corti
 */
public class PrbmUnit {

    /** Azimut angle of this PrbmUnit */
    private float azimut;
    /** Meters of this PrbmUnit */
    private float meter;
    /** Minutes elapsed of this PrbmUnit */
    private float minutes;

    /** List of Entities */
    private List<PrbmEntity> entities;


    // //////////////////////////////////////
    //
    // CONSTRUCTORS
    //
    // //////////////////////////////////////

    /**
     * Base constructor for a new PrbmUnit
     * @param azimut  Azimut angle
     * @param meter   Meters
     * @param minutes Minutes elapsed
     */
    public PrbmUnit(float azimut, float meter, float minutes) {
        this.azimut = azimut;
        this.meter = meter;
        this.minutes = minutes;
        this.entities = new ArrayList<>();
    }

    /**
     * Base constructor for a new PrbmUnit
     * sets all parameters to 0
     */
    public PrbmUnit(){
        this(0,0,0);
    }

    /**
     * Return JSON representation of a single PrbmUnit
     * @return A JSONObject from a PrbmUnit
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("azimut", azimut);
            jsonObject.put("meter", meter);
            jsonObject.put("minutes", minutes);

            JSONArray jsonArray = new JSONArray();
            for (PrbmEntity e : entities) {
                jsonArray.put(e.toJSONObject());
            }
            jsonObject.put("entities", jsonArray);

            return jsonObject;
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }

    public float getAzimut() {
        return azimut;
    }

    public float getMeter() {
        return meter;
    }

    public float getMinutes() {
        return minutes;
    }
}