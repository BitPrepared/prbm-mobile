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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class related to model. PrbmUnit represent a single row of
 * Prbm that is composed by several PrbmEntity
 * @author Nicola Corti
 */
public class PrbmUnit implements Serializable {

    /** Azimut angle of this PrbmUnit */
    private float azimut;
    /** Meters of this PrbmUnit */
    private float meter;
    /** Minutes elapsed of this PrbmUnit */
    private float minutes;

    /** List of Entities far Left */
    private List<PrbmEntity> entitiesFarLeft;
    /** List of Entities far Right */
    private List<PrbmEntity> entitiesFarRight;
    /** List of Entities near Left */
    private List<PrbmEntity> entitiesNearLeft;
    /** List of Entities near Left */
    private List<PrbmEntity> entitiesNearRight;


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
        this.entitiesFarLeft = new ArrayList<>();
        this.entitiesFarRight = new ArrayList<>();
        this.entitiesNearLeft = new ArrayList<>();
        this.entitiesNearRight = new ArrayList<>();
    }

    /**
     * Base constructor for a new PrbmUnit
     * sets all parameters to 0
     */
    public PrbmUnit() {
        this(0, 0, 0);
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
            for (PrbmEntity e : entitiesFarLeft) {
                jsonArray.put(e.toJSONObject());
            }
            jsonObject.put("entities-far-left", jsonArray);
            jsonArray = new JSONArray();
            for (PrbmEntity e : entitiesNearLeft) {
                jsonArray.put(e.toJSONObject());
            }
            jsonObject.put("entities-near-left", jsonArray);
            jsonArray = new JSONArray();
            for (PrbmEntity e : entitiesNearRight) {
                jsonArray.put(e.toJSONObject());
            }
            jsonObject.put("entities-near-right", jsonArray);
            jsonArray = new JSONArray();
            for (PrbmEntity e : entitiesFarRight) {
                jsonArray.put(e.toJSONObject());
            }
            jsonObject.put("entities-far-right", jsonArray);

            return jsonObject;
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Getter for Unit Azimut
     * @return The unit azimut
     */
    public float getAzimut() {
        return azimut;
    }

    /**
     * Getter for Unit Meters
     * @return The unit meters
     */
    public float getMeter() {
        return meter;
    }

    /**
     * Getter for Unit Minutes
     * @return The unit minutes
     */
    public float getMinutes() {
        return minutes;
    }

    /**
     * Setter for Unit Azimut
     * @param azimut The new unit azimut
     */
    public void setAzimut(String azimut) {
        this.azimut = Float.parseFloat(azimut);
    }

    /**
     * Setter for Unit Minutes
     * @param minutes The new unit minutes
     */
    public void setMinutes(String minutes) {
        this.minutes = Float.parseFloat(minutes);
    }

    /**
     * Setter for Unit Meters
     * @param meter The new unit meters
     */
    public void setMeters(String meter) {
        this.meter = Float.parseFloat(meter);
    }

    /**
     * Getter for Far Left Entity List
     * @return The Far Left Entity List
     */
    public List<PrbmEntity> getFarLeft() {
        return entitiesFarLeft;
    }

    /**
     * Getter for Far Right Entity List
     * @return The Far Right Entity List
     */
    public List<PrbmEntity> getFarRight() {
        return entitiesFarRight;
    }

    /**
     * Getter for Near Left Entity List
     * @return The Near Left Entity List
     */
    public List<PrbmEntity> getNearLeft() {
        return entitiesNearLeft;
    }

    /**
     * Getter for Near Right Entity List
     * @return The Near Right Entity List
     */
    public List<PrbmEntity> getNearRight() {
        return entitiesNearRight;
    }

    /**
     * Add an entity in a specified column
     * @param entity Entity to add
     * @param column Involved column (0-3)
     */
    public void addEntity(PrbmEntity entity, int column) {
        if (entity == null) return;
        List<PrbmEntity> toAdd = null;
        switch (column) {
            case 0:
                toAdd = entitiesFarLeft;
                break;
            case 1:
                toAdd = entitiesNearLeft;
                break;
            case 2:
                toAdd = entitiesNearRight;
                break;
            case 3:
                toAdd = entitiesFarRight;
                break;
        }
        if (toAdd != null) toAdd.add(entity);
    }

    /**
     * Getter for entity list from a specified column
     * @param column Desired column (0-3)
     */
    public List<PrbmEntity> getEntitiesFromColumn(int column) {
        List<PrbmEntity> toReturn = null;
        switch (column) {
            case 0:
                toReturn = entitiesFarLeft;
                break;
            case 1:
                toReturn = entitiesNearLeft;
                break;
            case 2:
                toReturn = entitiesNearRight;
                break;
            case 3:
                toReturn = entitiesFarRight;
                break;
        }
        return toReturn;
    }

    /**
     * Delete an entity from a specified column
     * @param entity Entity to delete
     * @param column Involved column (0-3)
     */
    public void deleteEntity(PrbmEntity entity, int column) {
        if (entity == null) return;
        List<PrbmEntity> toDelete = null;
        switch (column) {
            case 0:
                toDelete = entitiesFarLeft;
                break;
            case 1:
                toDelete = entitiesNearLeft;
                break;
            case 2:
                toDelete = entitiesNearRight;
                break;
            case 3:
                toDelete = entitiesFarRight;
                break;
        }
        if (toDelete != null) toDelete.remove(entity);
    }

    /**
     * Move an entity within a specified column
     * @param entity Entity to add
     * @param column Involved column (0-3)
     * @param down   Boolean flag to specify if movement must be downside or upside
     */
    public void moveEntity(PrbmEntity entity, int column, boolean down) {
        if (entity == null) return;
        List<PrbmEntity> toMove = null;
        switch (column) {
            case 0:
                toMove = entitiesFarLeft;
                break;
            case 1:
                toMove = entitiesNearLeft;
                break;
            case 2:
                toMove = entitiesNearRight;
                break;
            case 3:
                toMove = entitiesFarRight;
                break;
        }
        if (toMove != null) {
            int index = toMove.indexOf(entity);
            if (down)
                Collections.swap(toMove, index, index + 1);
            else
                Collections.swap(toMove, index, index - 1);
        }
    }
}