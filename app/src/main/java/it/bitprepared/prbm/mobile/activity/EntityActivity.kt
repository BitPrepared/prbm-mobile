package it.bitprepared.prbm.mobile.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.get
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
import it.bitprepared.prbm.mobile.databinding.ActivityEntityBinding
import it.bitprepared.prbm.mobile.databinding.EditGenericFieldBinding
import it.bitprepared.prbm.mobile.model.PrbmEntityField
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalTime


/**
 * Class responsible for visualizing and modifying a single PrbmEntity
 */
class EntityActivity : AppCompatActivity() {
  private lateinit var binding: ActivityEntityBinding
  private val viewModel: EntityViewModel by viewModels()

  private lateinit var capturedImageUri: Uri
  private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

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
            state.images.forEach {
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
              cardView.addView(imageView)
              Glide.with(this@EntityActivity).load(it)
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
      selectFromGallery()
    }
    pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
      if (uri != null) {
        viewModel.addImage(uri)
      }
    }
  }

  private fun renderFields(fields: List<PrbmEntityField>, fieldValues: Map<String, String>) {
    binding.linFree.removeAllViews()
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

  private fun selectFromGallery() {
    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
  }

  @Deprecated("Deprecated in Java")
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
      viewModel.addImage(capturedImageUri)
    }
  }

  companion object {
    private const val CAMERA_RESULT = 1005
  }
}
