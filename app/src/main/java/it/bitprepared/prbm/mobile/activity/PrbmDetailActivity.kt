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
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import it.bitprepared.prbm.mobile.databinding.ModifyUnitValuesBinding
import it.bitprepared.prbm.mobile.model.PrbmUnit
import kotlinx.coroutines.launch

/**
 * Class responsible for visualizing a single Prbm
 */
class PrbmDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPrbmBinding
    private lateinit var adtPrbmUnit: PrbmUnitAdapter

    private val viewModel: PrbmDetailViewModel by viewModels()

    private lateinit var adtUnit: PrbmUnitAdapter

    /** Reference to value edit dialog  */
    private lateinit var valueDialog: AlertDialog

    /** Reference to Location Manager  */
    private lateinit var locationManager: LocationManager

    private lateinit var azimuthInput: EditText
    private lateinit var metersInput: EditText
    private lateinit var minutesInput: EditText

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
                    }
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

        val refPrbm = UserData.prbm
        if (refPrbm != null) {
            adtPrbmUnit = PrbmUnitAdapter(refPrbm.units, LayoutInflater.from(this))
            binding.lstUnits.setLayoutManager(LinearLayoutManager(this))
            val divider = MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL)
            binding.lstUnits.addItemDecoration(divider)
            binding.lstUnits.setAdapter(adtPrbmUnit)
        }

        // Building dialog for unit value edit
        val alertValuesBuilder = MaterialAlertDialogBuilder(this@PrbmDetailActivity)
        val dialogBinding = ModifyUnitValuesBinding.inflate(layoutInflater)
        azimuthInput = dialogBinding.azimuthInput
        metersInput = dialogBinding.meterInput
        minutesInput = dialogBinding.minutesInput

        alertValuesBuilder.setMessage(getString(R.string.modify_entity_values))
        alertValuesBuilder.setIcon(R.drawable.ic_compass_outline_black_48dp)
        alertValuesBuilder.setView(dialogBinding.root)
        alertValuesBuilder.setNegativeButton(R.string.abort) { dialog, _ -> dialog.dismiss() }
        alertValuesBuilder.setPositiveButton(R.string.ok) { dialog, _ -> // Update Unit values
            val unit = UserData.unit ?: error("Unit is null - App should crash")
            unit.setAzimuth(dialogBinding.azimuthInput.text.toString().replace(',', '.'))
            unit.setMinutes(dialogBinding.minutesInput.text.toString().replace(',', '.'))
            unit.setMeters(dialogBinding.meterInput.text.toString().replace(',', '.'))
            dialog.dismiss()
            adtUnit.notifyDataSetChanged()
        }
        valueDialog = alertValuesBuilder.create()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateContextMenu(
        menu: ContextMenu, v: View, menuInfo: ContextMenuInfo
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v.id == R.id.lstUnits) {
            menu.add(Menu.NONE, MENU_UNIT_EDIT, 0, getString(R.string.modify_values))
            menu.add(Menu.NONE, MENU_UNIT_ADD_AFTER, 0, getString(R.string.insert_unit_up))
            menu.add(Menu.NONE, MENU_UNIT_ADD_BEFORE, 0, getString(R.string.insert_unit_down))
            menu.add(Menu.NONE, MENU_UNIT_DELETE, 0, getString(R.string.delete_row))
            menu.add(Menu.NONE, MENU_UNIT_GPS, 0, R.string.get_gps_coord)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterContextMenuInfo?

        val refPrbm = UserData.prbm
        if (item.itemId == MENU_UNIT_EDIT) {
            // Show value dialog
            val unit = refPrbm!!.getUnit(info!!.position)
            UserData.unit = unit
            metersInput.setText(unit.meter.toString())
            azimuthInput.setText(unit.azimuth.toString())
            minutesInput.setText(unit.minutes.toString())
            valueDialog.show()
        } else if (item.itemId == MENU_UNIT_ADD_AFTER) {
            // Add a unit after
            refPrbm!!.addNewUnits(info!!.position, false)
            adtUnit.notifyDataSetChanged()
        } else if (item.itemId == MENU_UNIT_ADD_BEFORE) {
            // Add a unit before
            refPrbm!!.addNewUnits(info!!.position, true)
            adtUnit.notifyDataSetChanged()
        } else if (item.itemId == MENU_UNIT_DELETE) {
            // Delete a unit
            if (refPrbm!!.canDelete()) {
                refPrbm.deleteUnit(info!!.position)
                adtUnit.notifyDataSetChanged()
            } else {
                // Tip to don't delete last unit
                Toast.makeText(
                    this, getString(R.string.you_cant_delete_last_unit), Toast.LENGTH_SHORT
                ).show()
            }
        } else if (item.itemId == MENU_UNIT_GPS) {
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


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_ADD_ENTITY || requestCode == ACTIVITY_MODIFY_ENTITY) {
            // Update adapter on activity result
            adtUnit.notifyDataSetChanged()
        }
    }

    companion object {
        /** Flag used for context menu - Edit unit  */
        private const val MENU_UNIT_EDIT = 1

        /** Flag used for context menu - Add unit before  */
        private const val MENU_UNIT_ADD_BEFORE = 2

        /** Flag used for context menu - Add unit after  */
        private const val MENU_UNIT_ADD_AFTER = 3

        /** Flag used for context menu - Delete Unit  */
        private const val MENU_UNIT_DELETE = 4

        /** Flag used for context menu - Obtain GPS  */
        private const val MENU_UNIT_GPS = 5

        /** Flag used for activity add Entity  */
        const val ACTIVITY_ADD_ENTITY: Int = 101

        /** Flag used for activity modify Entity  */
        const val ACTIVITY_MODIFY_ENTITY: Int = 102
    }
}
