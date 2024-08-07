package it.bitprepared.prbm.mobile.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.activity.UserData.entity
import it.bitprepared.prbm.mobile.databinding.ActivityEntityBinding
import it.bitprepared.prbm.mobile.databinding.EditGenericFieldBinding
import it.bitprepared.prbm.mobile.model.PrbmEntityField
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalTime
import java.util.Calendar


/**
 * Class responsible for visualizing and modifying a single PrbmEntity
 */
class EntityActivity : AppCompatActivity() {
  private lateinit var binding: ActivityEntityBinding
  private val viewModel: EntityViewModel by viewModels()

  private lateinit var capturedImageUri: Uri

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityEntityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.modelState.collect { state ->
          if (state.saveReady) {
            finish()
          } else {
            binding.topAppBar.title = state.typeDescription
            binding.editTime.text = state.time
            binding.edtTitle.setTextIfDifferent(state.title)
            binding.edtDescription.setTextIfDifferent(state.description)
            renderFields(state.fields, state.fieldValues)
            binding.topAppBar.menu[0].isVisible = state.isEditing
            binding.topAppBar.setOnMenuItemClickListener { menuItem ->
              when (menuItem.itemId) {
                R.id.delete -> {
                  confirmDelete()
                  true
                }
                else -> false
              }
            }
            while (binding.linGallery.childCount > 2) {
              binding.linGallery.removeViewAt(2)
            }
            state.images.forEach {
              val px8 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8F,
                this@EntityActivity.getResources().displayMetrics
              ).toInt()
              val px16 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16F,
                this@EntityActivity.getResources().displayMetrics
              ).toInt()
              val px80 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                80F,
                this@EntityActivity.getResources().displayMetrics
              ).toInt()
              val cardView = CardView(this@EntityActivity).apply {
                radius = px8.toFloat()
                layoutParams = LinearLayout.LayoutParams(px80, px80).apply {
                  marginStart = px16
                }
              }
              val imageView = ImageView(this@EntityActivity).apply {
                layoutParams = ViewGroup.LayoutParams(
                  ViewGroup.LayoutParams.MATCH_PARENT,
                  ViewGroup.LayoutParams.MATCH_PARENT
                )
              }
              cardView.addView(imageView)
              Glide.with(this@EntityActivity)
                .load(it)
                .apply(RequestOptions().override(80, 80).centerCrop())
                .into(imageView)
              binding.linGallery.addView(cardView)
            }
          }
        }
      }
    }
    binding.edtTitle.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
      override fun afterTextChanged(s: android.text.Editable?) {
        viewModel.updateTitle(s.toString())
      }
    })
    binding.edtDescription.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
      override fun afterTextChanged(s: android.text.Editable?) {
        viewModel.updateDescription(s.toString())
      }
    })
    binding.editTime.setOnClickListener {
      val time = LocalTime.parse(binding.editTime.getText().toString())
      val timePicker =
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(time.hour)
          .setMinute(time.minute).setTitleText(R.string.pick_unit_time).build()
      timePicker.addOnPositiveButtonClickListener { _ ->
        viewModel.updateTime(timePicker.hour, timePicker.minute)
      }
      timePicker.show(supportFragmentManager, "timePicker")
    }
    binding.topAppBar.setNavigationOnClickListener {
      confirmExit()
    }
    binding.btnSaveEntity.setOnClickListener {
      viewModel.saveEntity()
    }
    binding.cardCamera.setOnClickListener {
      takePicture()
    }
    binding.cardGallery.setOnClickListener {

    }
  }

  private fun renderFields(fields: List<PrbmEntityField>, fieldValues: Map<String, String>) {
    fields.forEach { field ->
      val fieldBinding = EditGenericFieldBinding.inflate(layoutInflater, binding.linFree, false)
      fieldBinding.textinputGeneric.hint = field.name
      fieldBinding.textinputGeneric.helperText = field.hint
      fieldBinding.edtFieldGeneric.inputType = if (field.type == "number") {
        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
      } else {
        InputType.TYPE_CLASS_TEXT
      }
      binding.linFree.addView(fieldBinding.root)
      fieldBinding.edtFieldGeneric.setText(fieldValues.getOrDefault(field.name, ""))
      fieldBinding.edtFieldGeneric.addTextChangedListener {
        viewModel.updateFieldValue(field.name, it.toString())
      }
    }
    // We add some padding so the "Save" button is not always overlapping.
    val emptyView = View(this)
    emptyView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120)
    binding.linFree.addView(emptyView)
  }

  private fun confirmExit() =
    MaterialAlertDialogBuilder(this).setTitle(R.string.confirmation).setIcon(R.drawable.ic_alert)
      .setMessage(R.string.are_you_sure)
      .setPositiveButton(getString(R.string.exit_without_save)) { _, _ ->
        finish()
      }.setNegativeButton(R.string.abort) { _, _ -> }.create().show()

  private fun confirmDelete() =
    MaterialAlertDialogBuilder(this).setTitle(R.string.confirm_delete).setIcon(R.drawable.ic_delete)
      .setMessage(R.string.are_you_sure_delete_entity)
      .setPositiveButton(getString(R.string.delete)) { _, _ ->
        viewModel.deleteEntity()
      }.setNegativeButton(R.string.abort) { _, _ -> }.create().show()

  private fun getNewImageFilename() = "prbm_${System.currentTimeMillis()}.jpg"

  private fun getNewImageUri(): Uri {
    val fileName = getNewImageFilename()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
      }
      return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
    } else {
      val imagePath = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
      return Uri.fromFile(File(imagePath, fileName))
    }
  }

  private fun takePicture() {
    val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    capturedImageUri = getNewImageUri()
    i.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri)
    // TODO Move to Activity Result API
    startActivityForResult(i, CAMERA_RESULT)
  }


  @Deprecated("Deprecated in Java")
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    val entity = entity

    if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
      viewModel.addImage(capturedImageUri)
    } else if (requestCode == GALLERY_RESULT) {
//      if (resultCode != RESULT_OK) {
//        Toast.makeText(
//          this, "Impossibile caricare l'immagine dalla galleria", Toast.LENGTH_SHORT
//        ).show()
//        return
//      }
//      if (data == null) {
//        Toast.makeText(this, "Impossibile caricare l'immagine", Toast.LENGTH_SHORT).show()
//        return
//      }
//      capturedImageUri = data.data
//      binding.imgCamera.isVisible = true
//      Glide.with(this).load(data.data).apply(RequestOptions().override(600, 300).centerCrop())
//        .into(binding.imgCamera)
//
//      val cal = Calendar.getInstance()
//      val root = Environment.getExternalStorageDirectory()
//      val dir = File(root.absolutePath + "/PRBM")
//      dir.mkdirs()
//      val picname = cal.timeInMillis.toString() + ".jpg"
//      val destfile = File(dir, (cal.timeInMillis.toString() + ".jpg"))
//
//      if (!destfile.exists()) {
//        try {
//          destfile.createNewFile()
//        } catch (e: IOException) {
//          e.printStackTrace()
//        }
//      } else {
//        destfile.delete()
//        try {
//          destfile.createNewFile()
//        } catch (e: IOException) {
//          e.printStackTrace()
//        }
//      }
//      try {
//        copy(File(getRealPathFromUri(this, capturedImageUri)), destfile)
//      } catch (e: IOException) {
//        e.printStackTrace()
//      }
//      entity!!.pictureName = picname
    }
  }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        when (id) {
////            R.id.pic -> {
////                val entity = entity
////                val itemadd = arrayOf<CharSequence>(
////                    "Scatta una foto", "Scegli dalla Galleria"
////                )
////                val itemadddelete = arrayOf<CharSequence>(
////                    "Scatta una foto", "Scegli dalla Galleria", "Rimuovi foto"
////                )
////                val deleteflag = (entity != null && entity.pictureName.isNotEmpty())
////                val builder = MaterialAlertDialogBuilder(this)
////                builder.setIcon(R.drawable.ic_camera_iris_black_36dp)
////                builder.setTitle("Fotografia")
////                builder.setItems(
////                    (if (deleteflag) itemadddelete else itemadd)
////                ) { dialog: DialogInterface, which: Int ->
////                    when (which) {
////                        0 -> {
////                            takePicture()
////                            dialog.dismiss()
////                        }
////
////                        1 -> {
////                            chooseFromGallery()
////                            dialog.dismiss()
////                        }
////
////                        2 -> {
////                            entity!!.pictureName = ""
////                            binding.imgCamera.isGone = true
////                            dialog.dismiss()
////                        }
////
////                        else -> {}
////                    }
////                }
////                val alert = builder.create()
////                alert.setCancelable(true)
////                alert.show()
////            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

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

  companion object {
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
