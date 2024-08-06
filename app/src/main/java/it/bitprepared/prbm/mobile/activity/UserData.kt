package it.bitprepared.prbm.mobile.activity

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import it.bitprepared.prbm.mobile.model.PrbmEntity
import it.bitprepared.prbm.mobile.model.PrbmEntityType
import it.bitprepared.prbm.mobile.model.PrbmEntityTypeHolder
import it.bitprepared.prbm.mobile.model.Prbm
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

    /** Flag to check if user is editing a PRBM */
    var editPrbm: Boolean = false

    /** Reference to the [PrbmEntity] user is editing  */
    // TODO: Move me to ViewModel
    @JvmStatic
    var entity: PrbmEntity? = null

    fun newEntityFromPosition(position: Int) {
        val type = entityTypes[position]
        entity = PrbmEntity(type)
    }

    lateinit var entityTypes: List<PrbmEntityType>

    fun parseSchemaFromResources(context: Context) {
        context.resources.assets.open("prbm-entity-schema.json").use {
            entityTypes = gson.fromJson(it.reader(), PrbmEntityTypeHolder::class.java).entities
        }
    }

    fun getSingleChoiceEntityType(): Array<String> = entityTypes.map { it.name }.toTypedArray()

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
//        val toCheck = unit?.getEntitiesFromColumn(this.column) ?: return false
//        return toCheck.indexOf(entity) > 0
        // TODO Remove me?
        return false
    }

    /**
     * Check if actual unit can be moved down in its column
     * @return True if actual unit can be moved down
     */
    @JvmStatic
    fun canMoveUnitDown(): Boolean {
//        val toCheck = unit?.getEntitiesFromColumn(this.column) ?: return false
//        return toCheck.indexOf(entity) < toCheck.size - 1
        // TODO Remove me?
        return false
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

    private fun getPrbmDirectory(context: Context): File {
        if (!isExternalStorageWritable()) error("External storage not writable")
        val rootDirectory = context.applicationContext.getExternalFilesDir("")
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
            // TODO Move me to PRBM class and unify filename
            File(prbmDirectory, "${it.uuid}.json").apply {
                createNewFile()
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
        prbmDirectory.walk().forEach { file ->
            if (file.isFile && file.extension == "json") {
                val prbm = gson.fromJson(file.readText(), Prbm::class.java)
                _prbmList.add(prbm)
            }
        }
    }
}
