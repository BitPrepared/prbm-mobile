package it.bitprepared.prbm.mobile.activity

import android.content.Context
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

    /** Flag to check if user is editing a PRBM Unit */
    var editEntity: Boolean = false

    /** Reference to the [PrbmEntity] user is editing  */
    // TODO: Move me to ViewModel
    @JvmStatic
    var entity: PrbmEntity? = null

    private val _entityTypes: MutableList<PrbmEntityType> = mutableListOf()
    fun getEntityTypes(context: Context) : List<PrbmEntityType> {
        if (_entityTypes.isEmpty()) {
            context.resources.assets.open("prbm-entity-schema.json").use {
                _entityTypes.addAll(gson.fromJson(it.reader(), PrbmEntityTypeHolder::class.java).entities)
            }
        }
        return _entityTypes.toList()
    }
    fun getEntityTypes() = _entityTypes.toList()

    fun newEntityFromMenuIndex(context: Context, position: Int) = PrbmEntity(getEntityTypes(context)[position])

    fun getSingleChoiceEntityType(context: Context): Array<String> = getEntityTypes(context).map { it.name }.toTypedArray()

    /** Reference to actual unit  */
    // TODO: Move me to ViewModel
    @JvmStatic
    var unit: PrbmUnit? = null

    /** Returns reference to actual entity column (1-4) */
    @JvmStatic
    var column: Int = 0

    /** Base URL for HTTP Calls  */
    private const val API_BASE_URL = "http://prbmm.bitprepared.it/"

    val gson: Gson = GsonBuilder()
        .setStrictness(Strictness.LENIENT)
        .create()

    val restInterface: RemoteInterface by lazy {
        val retrofit = Retrofit.Builder().baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(RemoteInterface::class.java)
    }

    /**
     * Store actual PRBM and serialize the PRBM list
     */
    @JvmStatic
    fun savePrbm(context: Context, prbm: Prbm) {
        _prbmList.removeIf { it.uuid == prbm.uuid }
        _prbmList.add(prbm)
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
