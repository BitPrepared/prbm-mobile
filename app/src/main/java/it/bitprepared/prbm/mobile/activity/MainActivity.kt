package it.bitprepared.prbm.mobile.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.util.Linkify
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.databinding.AboutDialogBinding
import it.bitprepared.prbm.mobile.databinding.ActivityMainBinding
import it.bitprepared.prbm.mobile.databinding.ProgressDialogBinding
import kotlinx.coroutines.launch

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
                val dialogBinding = ProgressDialogBinding.inflate(layoutInflater)
                val dialog = AlertDialog.Builder(this@MainActivity)
                    .setView(dialogBinding.root)
                    .create()
                viewModel.modelState.collect { (isUploading, progress, error) ->
                    if (isUploading && !dialog.isShowing) {
                        dialogBinding.progressBar.setProgressCompat(0, false)
                        dialog.show()
                    } else if (isUploading && dialog.isShowing) {
                        val progressPerc = (progress * 100).toInt()
                        dialogBinding.progressBar.setProgressCompat(progressPerc, false)
                        dialogBinding.progressText.text =
                            getString(R.string.loading_in_progress, progressPerc)
                    } else if (!isUploading) {
                        dialog.dismiss()
                    } else if (error != null) {
                        dialogBinding.progressText.text = getString(R.string.loading_error, error)
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

    private val necessaryPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val permissionsRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (!permissions.entries.any { !it.value }) {
                navigateToCreatePrbm()
            } else {
                showMissingPermissionDialog()
            }
        }

    private fun showMissingPermissionDialog() =
        AlertDialog.Builder(this)
            .setTitle("Permessi mancanti")
            .setMessage("Per poter creare un nuovo PRBM è prima necessario accettare tutti i permessi richiesti.")
            .setPositiveButton(R.string.ok) { _, _ -> }
            .create()
            .show()

    private fun showAboutDialog() {
        val dialogBinding = AboutDialogBinding.inflate(layoutInflater)
        Linkify.addLinks(dialogBinding.text, Linkify.WEB_URLS)
        Linkify.addLinks(dialogBinding.webtext, Linkify.WEB_URLS)
        AlertDialog.Builder(this)
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
                Uri.parse("market://details?id=it.bitprepared.prbm.mobile")
            )
        )
    }
}