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

import org.json.JSONObject;

/**
 * Class related to model. PrbmEntity represent a single observation
 * that must be inserted in a column of a Prbm.
 * @author Nicola Corti
 */
public abstract class PrbmEntity {

    /** Entity description */
    private String description;
    /** Entity caption */
    private String caption;
    /** Entity timestamp */
    private String timestamp;

    // //////////////////////////////////////
    //
    // CONSTRUCTOR
    //
    // //////////////////////////////////////

    /**
     * Base constructor for a new PRBM Entity
     * @param description Entity description
     * @param caption     Entity caption
     * @param timestamp   Entity timestamp
     */
    public PrbmEntity(String description, String caption, String timestamp) {
        this.description = description;
        this.caption = caption;
        this.timestamp = timestamp;
    }

    /**
     * Returns JSON representation of a PrbmUnit instance.
     * @return A JSONObject from PrbmUnit
     */
    public abstract JSONObject toJSONObject();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public abstract String getType();
}
