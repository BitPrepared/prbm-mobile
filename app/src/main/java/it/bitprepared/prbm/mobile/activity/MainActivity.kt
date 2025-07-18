package it.bitprepared.prbm.mobile.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.util.Linkify
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.bitprepared.prbm.mobile.BuildConfig
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.databinding.AboutDialogBinding
import it.bitprepared.prbm.mobile.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import androidx.core.net.toUri

/**
 * Activity responsible for main menu visualization
 */
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.modelState.collect { (isUploading, progress, error) ->
          if (isUploading) {
            binding.progressCard.isVisible = true
          }
          binding.progressBar.setProgressCompat((progress * 100).toInt(), true)
          if (error != null) {
            binding.progressStatus.text = "Error: $error"
          } else {
            val progressPercent = (progress * 100).toInt()
            if (progressPercent < 100) {
              binding.progressStatus.text = "Caricamento in corso: ${progressPercent}%"
            } else {
              binding.progressStatus.text = "Caricamento completato!"
            }
          }
        }
      }
    }

    binding.btnNew.setOnClickListener {
      permissionsRequest.launch(necessaryPermissions)
    }
    binding.btnList.setOnClickListener {
      navigateToPrbmList()
    }
    binding.btnSyncro.setOnClickListener {
      viewModel.uploadPrbmJSONs(this@MainActivity)
    }
    binding.btnPrbmWeb.setOnClickListener {
      openPrbmWeb()
    }
    binding.btnAbout.setOnClickListener {
      showAboutDialog()
    }
  }

  private fun navigateToPrbmList() {
    startActivity(Intent(applicationContext, ListPrbmActivity::class.java))
  }

  private fun navigateToCreatePrbm() {
    startActivity(Intent(applicationContext, CreateEditPrbmActivity::class.java))
  }

  private val necessaryPermissions =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
      arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_MEDIA_IMAGES
      )
    } else {
      arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE
      )
    }

  private val permissionsRequest =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
      if (!permissions.entries.any { !it.value }) {
        navigateToCreatePrbm()
      } else {
        showMissingPermissionDialog()
      }
    }

  private fun showMissingPermissionDialog() =
    MaterialAlertDialogBuilder(this)
      .setTitle("Permessi mancanti")
      .setMessage("Per poter creare un nuovo PRBM è prima necessario accettare tutti i permessi richiesti.")
      .setPositiveButton(R.string.ok) { _, _ -> }
      .create()
      .show()

  private fun openPrbmWeb() {
    startActivity(
      Intent(
        Intent.ACTION_VIEW,
        "https://prbmm.bitprepared.it".toUri()
      )
    )
  }

  private fun showAboutDialog() {
    val dialogBinding = AboutDialogBinding.inflate(layoutInflater)
    Linkify.addLinks(dialogBinding.text, Linkify.WEB_URLS)
    Linkify.addLinks(dialogBinding.webtext, Linkify.WEB_URLS)
    dialogBinding.version.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    MaterialAlertDialogBuilder(this)
      .setView(dialogBinding.root)
      .setPositiveButton(R.string.contact) { _, _ -> sendContactEmail() }
      .setNegativeButton(R.string.close) { dialog, _ -> dialog.dismiss() }
      .setNeutralButton(R.string.rate) { _, _ -> rateOnPlayStore() }
      .create()
      .show()
  }

  private fun sendContactEmail() {
    val emailIntent = Intent(Intent.ACTION_SEND)
    emailIntent.setType("plain/text")
    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
    startActivity(Intent.createChooser(emailIntent, getString(R.string.sendmail)))
  }

  private fun rateOnPlayStore() {
    startActivity(
      Intent(
        Intent.ACTION_VIEW,
          "market://details?id=it.bitprepared.prbm.mobile".toUri()
      )
    )
  }
}
