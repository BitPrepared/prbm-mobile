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
package it.bitprepared.prbm.mobile.model

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.LinearLayout
import it.bitprepared.prbm.mobile.activity.EntityViewHelper
import java.io.File

/**
 * PrbmEntity represent a single observation
 * that must be inserted in a column of a Prbm.
 */
abstract class PrbmEntity (
    @JvmField var description: String = "",
    @JvmField var caption: String = "",
    @JvmField var timestamp: String = ""
) {

    var pictureName: String = ""

    /** The Entity Type name */
    abstract val type: String?

    /** The Entity Type description */
    abstract val typeDescription: String?

    /** The Resource ID for list image background */
    abstract val idListImage: Int

    /** The Resource ID for button background */
    abstract val idButtonImage: Int

    /** The Resource ID for entity activity background */
    abstract val idBackImage: Int

    /** The list of Extra Fields to be drawn by this PrbmEntity */
    abstract val extraFields: Array<EntityField>

    /**
     * Abstract method invoked when each entity must render itself.
     * Each entity can use a LinearLayout to draw its own fields
     * @param context Execution Context
     * @param linFree Linear Layout that can be used to draw views
     */
    fun drawYourSelf(context: Context?, linFree: LinearLayout?) {
        for (field in extraFields) {
            EntityViewHelper.addExtraField(context, linFree, field)
        }
    }

    /**
     * Invoked when each entity must save its own fields.
     */
    fun saveFields(context: Context, layout: LinearLayout) {
        EntityViewHelper.saveLinearLayoutFields(extraFields, layout)
    }

    /**
     * Invoked when each entity must restore its own fields.
     */
    fun restoreFields(context: Context, layout: LinearLayout) {
        EntityViewHelper.restoreLinearLayoutFields(extraFields, layout)
    }

    val pictureURI: Uri
        get() {
            if (pictureName.isNotEmpty()) {
                val root = Environment.getExternalStorageDirectory()
                // TODO Change directory name here
                val dir = File(root.absolutePath + "/PRBM")
                dir.mkdirs()
                return Uri.fromFile(File(dir, pictureName))
            } else {
                error("Cannot invoke .pictureURI on an entity without a pictureName")
            }
        }
}
