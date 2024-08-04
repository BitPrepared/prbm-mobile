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
data class NewPrbmEntity (
    @Volatile var type: NewPrbmEntityType,
    val typeId: String = type.id,
    var description: String = "",
    var pictureName: String = "",
    val fieldValues: MutableMap<String, String> = mutableMapOf()
) {

//    /**
//     * Abstract method invoked when each entity must render itself.
//     * Each entity can use a LinearLayout to draw its own fields
//     * @param context Execution Context
//     * @param linFree Linear Layout that can be used to draw views
//     */
//    fun drawYourSelf(context: Context?, linFree: LinearLayout?) {
//        for (field in extraFields) {
//            EntityViewHelper.addExtraField(context, linFree, field)
//        }
//    }
//
//    /**
//     * Invoked when each entity must save its own fields.
//     */
//    fun saveFields(context: Context, layout: LinearLayout) {
//        EntityViewHelper.saveLinearLayoutFields(extraFields, layout)
//    }
//
//    /**
//     * Invoked when each entity must restore its own fields.
//     */
//    fun restoreFields(context: Context, layout: LinearLayout) {
//        EntityViewHelper.restoreLinearLayoutFields(extraFields, layout)
//    }

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

