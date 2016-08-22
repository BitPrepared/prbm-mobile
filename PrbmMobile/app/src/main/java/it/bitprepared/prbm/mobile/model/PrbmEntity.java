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

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.LinearLayout;

import it.bitprepared.prbm.mobile.activity.EntityViewHelper;

import java.io.File;
import java.io.Serializable;

/**
 * Class related to model. PrbmEntity represent a single observation
 * that must be inserted in a column of a Prbm.
 * @author Nicola Corti
 */
public abstract class PrbmEntity implements Serializable {

    /** Entity description */
    private String description;
    /** Entity caption */
    private String caption;
    /** Entity timestamp */
    private String timestamp;
    /** Image URI */
    private transient Uri pictureURI;
    private String pictureName;
    private String picturePath;

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
     * Base constructor for an empty PRBM Entity
     */
    public PrbmEntity() {
        this("", "", "");
    }

    /**
     * Getter for Entity description
     * @return The Entity description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for Entity description
     * @param description The new entity description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for Entity caption
     * @return The Entity caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Setter for Entity caption
     * @param caption The new entity caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Getter for Entity timestamp
     * @return The Entity timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Setter for Entity timestamp
     * @param timestamp The new entity timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Abstract method for returing Entity Type name
     * @return The Type name of an entity
     */
    public abstract String getType();

    /**
     * Abstract method for returing Entity Type description
     * @return The Description of an entity
     */
    public abstract String getTypeDescription();

    /**
     * Abstract method for returing Resource ID for list image background
     * @return Resource ID for list image background
     */
    public abstract int getIdListImage();

    /**
     * Abstract method for returing Resource ID for button background
     * @return Resource ID for button background
     */
    public abstract int getIdButtonImage();

    /**
     * Abstract method for returing Resource ID for entity activity background
     * @return Resource ID for entity activity background
     */
    public abstract int getIdBackImage();

    /**
     * Abstract method for returning the list of Extra Fields to be drawn by this PrbmEntity
     * @return An array of EntityField
     */
    public abstract EntityField[] getExtraFields();

    /**
     * Abstract method invoked when each entity must render itself.
     * Each entity can use a LinearLayout to draw its own fields
     * @param context Execution Context
     * @param linFree Linear Layout that can be used to draw views
     */
    public void drawYourSelf(Context context, LinearLayout linFree){
        for(EntityField field : getExtraFields()){
            EntityViewHelper.addExtraField(context, linFree, field);
        }
    }

    /**
     * Abstract method invoked when each entity must save its own fields.
     * @param context Execution Context
     * @param linFree Linear Layout that must be used to save view values
     */
    public void saveFields(Context context, LinearLayout linFree){
        EntityViewHelper.saveLinearLayoutFields(getExtraFields(), linFree);
    }

    /**
     * Abstract method invoked when each entity must restore its own fields.
     * @param context Execution Context
     * @param linFree Linear Layout that must be used to restore view values
     */
    public void restoreFields(Context context, LinearLayout linFree){
        EntityViewHelper.restoreLinearLayoutFields(getExtraFields(), linFree);
    }

    public void setPictureURI(Uri pictureURI, String pictureName) {
        this.pictureURI = pictureURI;
        this.pictureName = pictureName;
        this.picturePath = ((pictureURI == null) ? null : pictureURI.getPath());
    }

    public Uri getPictureURI() {
        // Rebuild the URI if missing
        if (picturePath != null && pictureURI == null){
            pictureURI = Uri.fromFile(new File(picturePath));
        }
        Log.e("TAG", "URI " + pictureURI);
        Log.e("TAG", "PATH " + picturePath);
        Log.e("TAG", "NAME " + pictureName);
        return pictureURI;
    }
}
