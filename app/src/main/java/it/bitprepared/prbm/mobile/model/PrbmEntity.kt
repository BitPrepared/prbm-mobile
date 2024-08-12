package it.bitprepared.prbm.mobile.model

import android.database.Cursor
import android.provider.MediaStore
import it.bitprepared.prbm.mobile.activity.UserData
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


/**
 * PrbmEntity represent a single observation
 * that must be inserted in a column of a Prbm.
 */
data class PrbmEntity(
    val typeId: String,
    var time: String = "",
    var title: String = "",
    var description: String = "",
    var pictureUri: MutableList<String> = mutableListOf(),
    var pictureFilenames: MutableList<String> = mutableListOf(),
    val fieldValues: MutableMap<String, String> = mutableMapOf()
) {

    val type :PrbmEntityType
        get() = UserData.entityTypes.first { it.id == typeId }

    constructor(type: PrbmEntityType) : this(type.id) {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val now = ZonedDateTime.now()
        time = now.toLocalTime().format(timeFormatter)
    }

    fun populatePictures(imageUris: List<String>, imageFilenames: List<String>) {
        pictureUri.clear()
        pictureFilenames.clear()
        pictureUri.addAll(imageUris)
        pictureFilenames.addAll(imageFilenames)
    }
}

