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
package it.bitprepared.prbm.mobile.activity

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import it.bitprepared.prbm.mobile.model.Prbm
import it.bitprepared.prbm.mobile.model.PrbmEntity
import it.bitprepared.prbm.mobile.model.PrbmUnit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * Class used to collect global data related to user/application
 * TODO: Move me to either Koin or Hilt
 */
object UserData {

    private var _prbmList: MutableList<Prbm> = mutableListOf()

    /** List of PRBMs loaded */
    // TODO: Move me to ViewModel
    @JvmStatic
    val prbmList: List<Prbm> get() = _prbmList

    /** Reference to the [Prbm] user is editing */
    // TODO: Move me to ViewModel
    @JvmStatic
    var prbm: Prbm? = null

    /** Reference to the [PrbmEntity] user is editing  */
    // TODO: Move me to ViewModel
    @JvmStatic
    var entity: PrbmEntity? = null

    /** Reference to actual unit  */
    // TODO: Move me to ViewModel
    @JvmStatic
    var unit: PrbmUnit? = null

    /** Returns reference to actual entity column (1-4) */
    // TODO: Move me to ViewModel
    @JvmStatic
    var column: Int = 0

    /**
     * Set a loaded hashmap of bitmaps. The hashmap can be used later to improve
     * performances while retrieving bitmaps.
     */
    // TODO: Remove me, probably unnecessary
    @JvmStatic
    val backBitmaps: MutableMap<Int, Bitmap> = mutableMapOf()

    /** Base URL for HTTP Calls  */
    private const val API_BASE_URL = "http://prbmm.bitprepared.it/"

    val gson: Gson = GsonBuilder()
        .setStrictness(Strictness.LENIENT)
        .registerTypeAdapter(PrbmEntity::class.java, PrbmEntityDecoder())
        .create()

    val restInterface: RemoteInterface by lazy {
        val retrofit = Retrofit.Builder().baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        retrofit.create(RemoteInterface::class.java)
    }

    /**
     * Check if actual unit can be moved up in its column
     * @return True if actual unit can be moved up
     */
    @JvmStatic
    fun canMoveUnitUp(): Boolean {
        val toCheck = unit?.getEntitiesFromColumn(this.column) ?: return false
        return toCheck.indexOf(entity) > 0
    }

    /**
     * Check if actual unit can be moved down in its column
     * @return True if actual unit can be moved down
     */
    @JvmStatic
    fun canMoveUnitDown(): Boolean {
        val toCheck = unit?.getEntitiesFromColumn(this.column) ?: return false
        return toCheck.indexOf(entity) < toCheck.size - 1
    }

    /**
     * Store actual PRBM and serialize the PRBM list
     */
    @JvmStatic
    fun savePrbm(context: Context, prbm: Prbm) {
        if (prbm !in _prbmList) _prbmList.add(prbm)
        serializePrbms(context)
    }

    /**
     * Delete actual PRBM and serialize the PRBM list
     */
    @JvmStatic
    fun deletePrbm(context: Context, prbm: Prbm) {
        if (_prbmList.contains(prbm)) _prbmList.remove(prbm)
        serializePrbms(context)
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun getPrbmDirectory(context: Context) : File {
        if (!isExternalStorageWritable()) error("External storage not writable")
        val rootDirectory = context.applicationContext.getExternalFilesDir("");
        return File(rootDirectory, "prbms")
    }


    /**
     * Serialize all prbms to private files
     */
    @Synchronized
    fun serializePrbms(context: Context) {
        val prbmDirectory = getPrbmDirectory(context).apply {
            deleteRecursively()
            mkdirs()
        }
        _prbmList.onEach {
            File(prbmDirectory, "${it.title}-${it.authors}-${it.date}.json").apply {
                writeText(gson.toJson(it))
            }
        }
    }

    /**
     * Deserialize all [Prbm] from private files
     */
    fun restorePrbms(context: Context) {
        val prbmDirectory = getPrbmDirectory(context)
        if (prbmDirectory.isDirectory && prbmDirectory.list().isNullOrEmpty()) {
            // Nothing to restore
            return
        }
        _prbmList.clear()
        prbmDirectory.walk().onEach { file ->
            if (file.isFile && file.extension == "json") {
                val prbm = gson.fromJson(file.readText(), Prbm::class.java)
                _prbmList.add(prbm)
            }
        }
    }
}
