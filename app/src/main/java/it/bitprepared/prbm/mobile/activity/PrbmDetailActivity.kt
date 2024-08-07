package it.bitprepared.prbm.mobile.activity

import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.activity.UserData.prbm
import it.bitprepared.prbm.mobile.activity.UserData.savePrbm
import it.bitprepared.prbm.mobile.databinding.ActivityDetailPrbmBinding
import it.bitprepared.prbm.mobile.databinding.EditNumericFieldBinding
import it.bitprepared.prbm.mobile.model.PrbmEntity
import it.bitprepared.prbm.mobile.model.PrbmUnit
import kotlinx.coroutines.launch

/**
 * Class responsible for visualizing a single Prbm
 */
class PrbmDetailActivity : AppCompatActivity(), PrbmUnitAdapter.OnPrbmUnitListener {

    private lateinit var binding: ActivityDetailPrbmBinding
    private lateinit var adtPrbmUnit: PrbmUnitAdapter

    private val viewModel: PrbmDetailViewModel by viewModels()

    private lateinit var adtUnit: PrbmUnitAdapter

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPrbmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.modelState.collect { state ->
                    if (state.saveSuccessful == true) {
                        showSavedSuccessfully()
                    } else if (state.editReady == true) {
                        openPrbmEditActivity()
                    } else if (state.editUnitReady == true) {
                        openNewUnitActivity()
                    }
                    adtPrbmUnit.setNewData(state.prbm.units)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                confirmExit()
            }
        })

        binding.prbmAppBar.setNavigationOnClickListener {
            confirmExit()
        }
        binding.prbmAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {
                    viewModel.savePrbm(this@PrbmDetailActivity)
                    true
                }

                R.id.parameters -> {
                    viewModel.editPrbm()
                    true
                }

                else -> false
            }
        }

        adtPrbmUnit = PrbmUnitAdapter(LayoutInflater.from(this), this)
        binding.lstUnits.setLayoutManager(LinearLayoutManager(this))
        val divider = MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.lstUnits.addItemDecoration(divider)
        binding.lstUnits.setAdapter(adtPrbmUnit)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }



    override fun onCreateContextMenu(
        menu: ContextMenu, v: View, menuInfo: ContextMenuInfo
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v.id == R.id.lstUnits) {
            menu.add(Menu.NONE, MENU_UNIT_GPS, 0, R.string.get_gps_coord)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterContextMenuInfo?

        val refPrbm = UserData.prbm
        if (item.itemId == MENU_UNIT_GPS) {
            val unit = refPrbm!!.getUnit(info!!.position)
            if (unit.latitude != 0.0 && unit.longitude != 0.0) {
                MaterialAlertDialogBuilder(this).setTitle(getString(R.string.update_gps_coordinates))
                    .setMessage(getString(R.string.do_you_want_to_override_gps))
                    .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("Si") { _, _ -> requestGPSCoordinates(unit) }.create().show()
            } else {
                requestGPSCoordinates(unit)
            }
        }
        return true
    }

    private fun requestGPSCoordinates(unit: PrbmUnit) {
//        Toast.makeText(this, "Acquisizione GPS in corso...", Toast.LENGTH_LONG).show()
//        unit.isFlagAcquiringGPS = true
//        adtUnit!!.notifyDataSetInvalidated()
//
//        lm!!.requestSingleUpdate(LocationManager.GPS_PROVIDER, object : LocationListener {
//            override fun onLocationChanged(location: Location) {
//                unit.longitude = location.longitude
//                unit.latitude = location.latitude
//                unit.isFlagAcquiringGPS = false
//                adtUnit!!.notifyDataSetInvalidated()
//                Toast.makeText(this@PrbmDetailActivity, "Coordinate acquisite!", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
//            }
//
//            override fun onProviderEnabled(provider: String) {
//            }
//
//            override fun onProviderDisabled(provider: String) {
//            }
//        }, null)
    }

    private fun showSavedSuccessfully() {
        Snackbar.make(
            binding.root, getString(R.string.save_prbm_successful), Snackbar.LENGTH_SHORT
        ).show()
        viewModel.showSavedDone()
    }

    private fun confirmExit() = MaterialAlertDialogBuilder(this@PrbmDetailActivity)
            .setTitle(R.string.confirmation)
            .setIcon(R.drawable.ic_alert)
            .setMessage(R.string.are_you_sure)
            .setPositiveButton(getString(R.string.save_and_exit)) { _, _ ->
                savePrbm(this@PrbmDetailActivity, prbm!!)
                finish()
            }.setNegativeButton(R.string.abort) { _, _ -> }.create().show()

    private fun openPrbmEditActivity() {
        startActivity(Intent(this@PrbmDetailActivity, CreateEditPrbmActivity::class.java))
        viewModel.editPrbmStarted()
    }

    private fun openNewUnitActivity() {
        startActivity(Intent(this@PrbmDetailActivity, EntityActivity::class.java))
        viewModel.editEntityStarted()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_ADD_ENTITY || requestCode == ACTIVITY_MODIFY_ENTITY) {
            // Update adapter on activity result
            adtUnit.notifyDataSetChanged()
        }
    }

    override fun onClickMeters(value: Int, position: Int) {
        val binding = EditNumericFieldBinding.inflate(layoutInflater)
        binding.editField.setText(value.toString())
        binding.textFieldTitle.hint = getString(R.string.meters_label)

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.number_of_meters))
            .setView(binding.root)
            .setNegativeButton(getString(R.string.abort)) { _, _ -> }
            .setPositiveButton(getString(R.string.proceed)) { _ , _ ->
                val newValue = binding.editField.text.toString()
                viewModel.updateMeters(adtPrbmUnit.fromAdapterPositionToDataPosition(position), newValue)
            }
            .show()
    }

    override fun onClickAzimuth(value: Int, position: Int) {
        val binding = EditNumericFieldBinding.inflate(layoutInflater)
        binding.editField.setText(value.toString())
        binding.textFieldTitle.hint = getString(R.string.azimuth_label)

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.azimuth_explanation))
            .setView(binding.root)
            .setNegativeButton(getString(R.string.abort)) { _, _ -> }
            .setPositiveButton(getString(R.string.proceed)) { _ , _ ->
                val newValue = binding.editField.text.toString()
                viewModel.updateAzimuth(adtPrbmUnit.fromAdapterPositionToDataPosition(position), newValue)
            }
            .show()
    }

    override fun onClickMinutes(value: Int, position: Int) {
        val binding = EditNumericFieldBinding.inflate(layoutInflater)
        binding.editField.setText(value.toString())
        binding.textFieldTitle.hint = getString(R.string.minutes_label)

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.azimuth_explanation))
            .setView(binding.root)
            .setNegativeButton(getString(R.string.abort)) { _, _ -> }
            .setPositiveButton(getString(R.string.proceed)) { _ , _ ->
                val newValue = binding.editField.text.toString()
                viewModel.updateMinutes(adtPrbmUnit.fromAdapterPositionToDataPosition(position), newValue)
            }
            .show()
    }

    override fun onClickGps(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onAddUnitButtonClicked(position: Int) {
        viewModel.addUnitFromPlusPosition(adtPrbmUnit.fromAdapterPositionToDataPosition(position))
    }

    override fun onClickDelete(position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.delete_the_row))
            .setMessage(getString(R.string.are_you_sure_to_delete_a_row))
            .setNegativeButton(getString(R.string.abort)) { _, _ -> }
            .setPositiveButton(getString(R.string.ok)) { _ , _ ->
                viewModel.deleteUnit(adtPrbmUnit.fromAdapterPositionToDataPosition(position))
            }
            .show()
    }

    override fun onNewEntityClicked(position: Int, columnIndex: Int, selectedEntityOptions: Int) {
        val unitIndex = adtPrbmUnit.fromAdapterPositionToDataPosition(position)
        viewModel.addNewEntity(unitIndex, columnIndex, selectedEntityOptions)
    }

    override fun onEntityClicked(prbmEntity: PrbmEntity, position: Int, columnIndex: Int) {
        val unitIndex = adtPrbmUnit.fromAdapterPositionToDataPosition(position)
        viewModel.editEntity(prbmEntity, unitIndex, columnIndex)
    }

    companion object {
        /** Flag used for context menu - Obtain GPS  */
        private const val MENU_UNIT_GPS = 5

        /** Flag used for activity add Entity  */
        const val ACTIVITY_ADD_ENTITY: Int = 101

        /** Flag used for activity modify Entity  */
        const val ACTIVITY_MODIFY_ENTITY: Int = 102
    }
}
