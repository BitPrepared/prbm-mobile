package it.bitprepared.prbm.mobile.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.core.view.get
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.databinding.ActivityEntityBinding
import it.bitprepared.prbm.mobile.databinding.EditGenericFieldBinding
import it.bitprepared.prbm.mobile.model.PrbmEntityField
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.time.LocalTime


/**
 * Class responsible for visualizing and modifying a single PrbmEntity
 */
class EntityActivity : AppCompatActivity() {
  private lateinit var binding: ActivityEntityBinding
  private val viewModel: EntityViewModel by viewModels()

  private lateinit var latestUri: Uri
  private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
  private lateinit var takePicture: ActivityResultLauncher<Uri>

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
            binding.linGallery.removeAllViews()
            state.imageUris.forEach { image ->
              val px8 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8F, this@EntityActivity.getResources().displayMetrics
              ).toInt()
              val px16 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16F, this@EntityActivity.getResources().displayMetrics
              ).toInt()
              val px80 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 80F, this@EntityActivity.getResources().displayMetrics
              ).toInt()
              val cardView = CardView(this@EntityActivity).apply {
                radius = px8.toFloat()
                layoutParams = LinearLayout.LayoutParams(px80, px80).apply {
                  marginStart = px16
                }
              }
              val imageView = ImageView(this@EntityActivity).apply {
                layoutParams = ViewGroup.LayoutParams(
                  ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
              }
              cardView.setOnClickListener {
                MaterialAlertDialogBuilder(this@EntityActivity)
                  .setTitle("Rimuovere l'immagine?")
                  .setIcon(R.drawable.ic_delete)
                  .setMessage("Sei sicuro di voler cancellare questa immagine dall'osservazione?")
                  .setNegativeButton(R.string.abort) { dialog, _ -> dialog.dismiss() }
                  .setPositiveButton(R.string.delete) { _, _ -> viewModel.removeImage(image) }
                  .create()
                  .show()
              }
              cardView.addView(imageView)
              Glide.with(this@EntityActivity).load(image)
                .apply(RequestOptions().override(80, 80).centerCrop()).into(imageView)
              binding.linGallery.addView(cardView)
            }
          }
        }
      }
    }
    binding.edtTitle.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
      override fun afterTextChanged(s: Editable?) {
        viewModel.updateTitle(s.toString())
      }
    })
    binding.edtDescription.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
      override fun afterTextChanged(s: Editable?) {
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
      selectFromGallery()
    }

    pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
      if (uri != null) {
        viewModel.addImage(uri.toString(), getFilenameFromUri(uri))
      }
    }
    takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
      if (result) {
        viewModel.addImage(latestUri.toString(), getFilenameFromUri(latestUri))
      }
    }
  }

  private fun renderFields(
    fields: List<PrbmEntityField>,
    fieldValues: Map<String, String>
  ) {
    if (binding.linFree.childCount == 0) {
      fields.forEach { field ->
        val fieldBinding = EditGenericFieldBinding.inflate(layoutInflater, binding.linFree, false)
        fieldBinding.textinputGeneric.hint = field.name
        fieldBinding.textinputGeneric.helperText = field.hint
        fieldBinding.edtFieldGeneric.inputType = if (field.type == "number") {
          // TODO This doesn't work, fix it
          InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
        } else {
          InputType.TYPE_CLASS_TEXT
        }
        binding.linFree.addView(fieldBinding.root)
        fieldBinding.edtFieldGeneric.setText(fieldValues.getOrDefault(field.name, ""))
        fieldBinding.edtFieldGeneric.addTextChangedListener(object : TextWatcher {
          override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
          override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
          override fun afterTextChanged(s: Editable?) {
            viewModel.updateFieldValue(field.name, s.toString())
          }
        })
      }
    } else {
      fields.forEachIndexed { index, field ->
        val editText = (binding.linFree.getChildAt(index) as TextInputLayout).editText
        editText?.setTextIfDifferent(fieldValues.getOrDefault(field.name, ""))
      }
    }
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

  private fun getNewImageUri(): Uri =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
      getNewImageUriWithFileProvider()
    } else {
      getNewImageUriWithFileUri()
    }

  private fun getNewImageUriWithFileProvider(): Uri {
    val tempImagesDir = File(
      applicationContext.filesDir, //this function gets the external cache dir
      "prbmm_images"
    ) //gets the directory for the temporary images dir
    tempImagesDir.mkdir() //Create the temp_images dir
    val tempImage = File(
      tempImagesDir, //prefix the new abstract path with the temporary images dir path
      getNewImageFilename()
    ) //gets the abstract temp_image file name

    return FileProvider.getUriForFile(
      applicationContext,
      "it.bitprepared.prbm.mobile.provider",
      tempImage
    )
  }

  private fun getNewImageUriWithFileUri(): Uri {
    val fileName = getNewImageFilename()
    val root = Environment.getExternalStorageDirectory()
    val imagesFolder = File(root, "prbm_images").apply {
      mkdirs()
    }
    return Uri.fromFile(File(imagesFolder, fileName))
  }

  private fun takePicture() {
    latestUri = getNewImageUri()
    takePicture.launch(latestUri)
  }

  private fun selectFromGallery() {
    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
  }

  private fun getFilenameFromUri(uri: Uri): String? =
    if (uri.lastPathSegment != null && uri.lastPathSegment!!.endsWith(".jpg")) {
      uri.lastPathSegment
    } else {
      getNewImageFilename()
    }
}
