package it.bitprepared.prbm.mobile.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.activity.UserData.column
import it.bitprepared.prbm.mobile.activity.UserData.entity
import it.bitprepared.prbm.mobile.activity.UserData.unit
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Class responsible for visualizing and modifying a single PrbmEntity
 */
class EntityActivity : AppCompatActivity() {
    private lateinit var linFree: LinearLayout
    private lateinit var edtCaption: EditText
    private lateinit var edtDescription: EditText
    private lateinit var datTime: TimePicker
    private lateinit var imgCamera: ImageView
    private var edit = false

    @Transient
    private var capturedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        edit = intent.getBooleanExtra("edit", false)

        // Setting up Home back button
        val bar = actionBar
        bar?.setDisplayHomeAsUpEnabled(true)

        // Inflating views
        setContentView(R.layout.activity_entity)
        linFree = findViewById(R.id.linearFreeEntity)
        val imgBack = findViewById<ImageView>(R.id.imgEntity)
        val txtTitle = findViewById<TextView>(R.id.txtEntityTitleAdd)
        datTime = findViewById(R.id.datTimeEntity)
        edtCaption = findViewById(R.id.edtCaption)
        edtDescription = findViewById(R.id.edtDescription)
        imgCamera = findViewById(R.id.imgCamera)

        val entity = entity
        if (entity != null) {
            imgBack.setImageResource(entity.idBackImage)
            entity.drawYourSelf(this@EntityActivity, linFree)
            txtTitle.text = entity.type

            val gson = Gson()
            Log.e("TAG", gson.toJson(entity))

            if (!edit) {
                // Setting current hour
                val c = Calendar.getInstance(resources.configuration.locale)
                datTime.currentHour = c[Calendar.HOUR_OF_DAY]
                datTime.currentMinute = c[Calendar.MINUTE]
                datTime.setIs24HourView(true)
            } else {
                edtCaption.setText(entity.caption)
                edtDescription.setText(entity.description)
                if (entity.pictureName.isNotEmpty()) {
                    capturedImageUri = entity.pictureURI
                    imgCamera.setVisibility(View.VISIBLE)

                    Glide
                        .with(this)
                        .load(capturedImageUri)
                        .apply(RequestOptions().override(600, 300).centerCrop())
                        .into(imgCamera)
                }

                // Restoring timestamp to TimePicker
                val dateFormat = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
                )
                try {
                    val date = dateFormat.parse(entity.timestamp)
                    val cal = Calendar.getInstance(Locale.getDefault())
                    cal.time = date
                    datTime.currentHour = cal[Calendar.HOUR_OF_DAY]
                    datTime.currentMinute = cal[Calendar.MINUTE]
                } catch (_: ParseException) {
                }
                entity.restoreFields(this, linFree)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.prbm_entity, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.discard, android.R.id.home -> {
                val build = MaterialAlertDialogBuilder(this@EntityActivity)
                build.setTitle(R.string.confirmation)
                build.setMessage(R.string.are_you_sure)
                build.setPositiveButton(R.string.ok) { _: DialogInterface?, _: Int ->
                    val entity = entity
                    entity!!.pictureName = ""
                    finish()
                }
                build.setNegativeButton(R.string.abort) { _: DialogInterface?, _: Int -> }
                build.show()
                return true
            }
            R.id.save -> {
                // Checking if caption is empty

                if (edtCaption.text.isEmpty()) {
                    val alert = MaterialAlertDialogBuilder(this)
                    alert.setTitle(R.string.fields_incomplete)
                    alert.setMessage(getString(R.string.you_must_insert_caption))
                    alert.setIcon(R.drawable.ic_alert_black_48dp)
                    alert.setPositiveButton(
                        R.string.ok
                    ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                    alert.show()
                } else {
                    val entity = entity
                    if (entity != null) {
                        entity.saveFields(this@EntityActivity, linFree)
                        entity.caption = edtCaption.text.toString()
                        entity.description = edtDescription.text.toString()
                        val c = Calendar.getInstance(resources.configuration.locale)

                        // Saving timestamp. Date set at Unix epoch
                        c[1970, 1, 1, datTime.currentHour] = datTime.currentMinute
                        val dateFormat = SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss", resources.configuration.locale
                        )
                        entity.timestamp = dateFormat.format(c.time)

                        if (!edit) {
                            val involved = unit
                            involved!!.addEntity(entity, column)
                        }
                        setResult(RESULT_OK)
                        finish()
                    }
                }
                return true
            }
            R.id.pic -> {
                val entity = entity
                val itemadd = arrayOf<CharSequence>(
                    "Scatta una foto",
                    "Scegli dalla Galleria"
                )
                val itemadddelete = arrayOf<CharSequence>(
                    "Scatta una foto",
                    "Scegli dalla Galleria", "Rimuovi foto"
                )
                val deleteflag = (entity != null && entity.pictureName.isNotEmpty())
                val builder = MaterialAlertDialogBuilder(this)
                builder.setIcon(R.drawable.ic_camera_iris_black_36dp)
                builder.setTitle("Fotografia")
                builder.setItems(
                    (if (deleteflag) itemadddelete else itemadd)
                ) { dialog: DialogInterface, which: Int ->
                    when (which) {
                        0 -> {
                            takePicture()
                            dialog.dismiss()
                        }

                        1 -> {
                            chooseFromGallery()
                            dialog.dismiss()
                        }

                        2 -> {
                            entity!!.pictureName = ""
                            imgCamera.visibility = View.GONE
                            dialog.dismiss()
                        }

                        else -> {}
                    }
                }
                val alert = builder.create()
                alert.setCancelable(true)
                alert.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFilenameFromURI(uri: Uri?): String {
        if (uri == null) {
            return ""
        }
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                val columnIndex = cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && columnIndex != -1) {
                    result = cursor.getString(columnIndex)
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun chooseFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(Intent.createChooser(intent, "Choose from Gallery"), GALLERY_RESULT)
    }

    private fun takePicture() {
        val cal = Calendar.getInstance()
        val root = Environment.getExternalStorageDirectory()
        val dir = File(root.absolutePath + "/PRBM")
        dir.mkdirs()
        val picname = cal.timeInMillis.toString() + ".jpg"
        val destfile = File(dir, picname)

        if (!destfile.exists()) {
            try {
                destfile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            destfile.delete()
            try {
                destfile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        capturedImageUri = Uri.fromFile(destfile)
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        i.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri)
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(i, CAMERA_RESULT)
    }

    @Throws(IOException::class)
    fun copy(src: File?, dst: File?) {
        val `in`: InputStream = FileInputStream(src)
        val out: OutputStream = FileOutputStream(dst)
        // Transfer bytes from in to out
        val buf = ByteArray(1024)
        var len: Int
        while ((`in`.read(buf).also { len = it }) > 0) {
            out.write(buf, 0, len)
        }
        `in`.close()
        out.close()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val entity = entity

        if (requestCode == CAMERA_RESULT) {
            imgCamera.visibility = View.VISIBLE
            Glide
                .with(this)
                .load(capturedImageUri)
                .apply(RequestOptions().override(600, 300).centerCrop())
                .into(imgCamera)

            entity!!.pictureName = getFilenameFromURI(capturedImageUri)!!
            Log.d(TAG, "capturedImage " + capturedImageUri!!.path)
        } else if (requestCode == GALLERY_RESULT) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(
                    this,
                    "Impossibile caricare l'immagine dalla galleria",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (data == null) {
                Toast.makeText(this, "Impossibile caricare l'immagine", Toast.LENGTH_SHORT).show()
                return
            }
            capturedImageUri = data.data
            imgCamera.visibility = View.VISIBLE
            Glide
                .with(this)
                .load(data.data)
                .apply(RequestOptions().override(600, 300).centerCrop())
                .into(imgCamera)

            val cal = Calendar.getInstance()
            val root = Environment.getExternalStorageDirectory()
            val dir = File(root.absolutePath + "/PRBM")
            dir.mkdirs()
            val picname = cal.timeInMillis.toString() + ".jpg"
            val destfile = File(dir, (cal.timeInMillis.toString() + ".jpg"))

            if (!destfile.exists()) {
                try {
                    destfile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                destfile.delete()
                try {
                    destfile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            try {
                copy(File(getRealPathFromUri(this, capturedImageUri)), destfile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            entity!!.pictureName = picname
        }
    }

    companion object {
        private const val TAG = "EntityActivity"
        private const val CAMERA_RESULT = 1005
        private const val GALLERY_RESULT = 1006

        fun getRealPathFromUri(context: Context, contentUri: Uri?): String {
            var cursor: Cursor? = null
            try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                return cursor.getString(column_index)
            } finally {
                cursor?.close()
            }
        }
    }
}
