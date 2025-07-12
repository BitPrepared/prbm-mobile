package it.bitprepared.prbm.mobile.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.databinding.ActivityDetailPrbmBinding
import it.bitprepared.prbm.mobile.databinding.EditNumericFieldBinding
import it.bitprepared.prbm.mobile.model.PrbmEntity
import it.bitprepared.prbm.mobile.model.PrbmUnit
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Class responsible for visualizing a single Prbm
 */
class PrbmDetailActivity : AppCompatActivity(), PrbmUnitAdapter.OnPrbmUnitListener {

  private lateinit var binding: ActivityDetailPrbmBinding
  private lateinit var adtPrbmUnit: PrbmUnitAdapter

  private val viewModel: PrbmDetailViewModel by viewModels()

  private lateinit var locationManager: LocationManager
  private val locationListener: LocationListener = object : LocationListener {
    override fun onLocationChanged(location: Location) {
      viewModel.updateGpsCoordinates(
        this@PrbmDetailActivity,
        location.latitude,
        location.longitude,
        location.time,
        location.bearing,
        location.speed
      )
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    }

    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityDetailPrbmBinding.inflate(layoutInflater)
    setContentView(binding.root)

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.modelState.collect { state ->
          if (state.editReady == true) {
            openPrbmEditActivity()
          } else if (state.editUnitReady == true) {
            openNewUnitActivity()
          }
          if (state.errorMessage != null) {
            showErrorSnackbar(state.errorMessage)
          }
          when (state.gpsStatus) {
            GpsStatus.DISABLED -> {
              binding.prbmAppBar.menu.findItem(R.id.gps).setIcon(R.drawable.ic_gps_off)
              disableLocationManager()
            }
            GpsStatus.PAIRING -> {
              binding.prbmAppBar.menu.findItem(R.id.gps).setIcon(R.drawable.ic_gps_not_fixed)
              enableLocationEnable()
            }
            GpsStatus.FIXED -> {
              binding.prbmAppBar.menu.findItem(R.id.gps).setIcon(R.drawable.ic_gps_fixed)
            }
          }
          adtPrbmUnit.setNewData(state.prbm.units)
          if (state.rowEdited != null || state.rowInserted != null || state.rowDeleted != null) {
            if (state.rowEdited != null) {
              adtPrbmUnit.notifyItemChanged(state.rowEdited)
            }
            if (state.rowInserted != null) {
              adtPrbmUnit.notifyItemInserted(state.rowInserted)
            }
            if (state.rowDeleted != null) {
              adtPrbmUnit.notifyItemRemoved(state.rowDeleted)
            }
            viewModel.listAnimationDispatched()
          }
        }
      }
    }

    binding.prbmAppBar.setNavigationOnClickListener {
      finish()
    }
    binding.prbmAppBar.setOnMenuItemClickListener { menuItem ->
      when (menuItem.itemId) {
        R.id.parameters -> {
          viewModel.editPrbm()
          true
        }

        R.id.gps -> {
          viewModel.userToggledGps()
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

  override fun onResume() {
    super.onResume()
    adtPrbmUnit.notifyDataSetChanged()
    viewModel.onResume(this)
  }

  private fun showErrorSnackbar(@StringRes errorMessage: Int) {
    if (errorMessage == R.string.no_gps_coordinates) {
      Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT)
        .setAction(R.string.enable_gps) {
          viewModel.userToggledGps()
        }.show()
      viewModel.errorShown()
    } else {
      Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
      viewModel.errorShown()
    }
  }

  private fun enableLocationEnable() {

    if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      showErrorSnackbar(R.string.gps_permission_denied)
      viewModel.userToggledGps()
      return
    }
    locationManager.requestLocationUpdates(
      LocationManager.GPS_PROVIDER,
      10000, // 10 sec
      10f, // 10 meters
      locationListener
    )
  }

  private fun disableLocationManager() {
    locationManager.removeUpdates(locationListener)
  }

  private fun openPrbmEditActivity() {
    startActivity(Intent(this@PrbmDetailActivity, CreateEditPrbmActivity::class.java))
    viewModel.editPrbmStarted()
  }

  private fun openNewUnitActivity() {
    startActivity(Intent(this@PrbmDetailActivity, EntityActivity::class.java))
    viewModel.editEntityStarted()
  }

  override fun onClickMeters(unit: PrbmUnit, value: Double) {
    val binding = EditNumericFieldBinding.inflate(layoutInflater)
    binding.editField.setText(value.toString())
    binding.editField.inputType = android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
    binding.textFieldTitle.hint = getString(R.string.meters_label)

    MaterialAlertDialogBuilder(this)
      .setTitle(getString(R.string.number_of_meters))
      .setView(binding.root)
      .setNegativeButton(getString(R.string.abort)) { _, _ -> }
      .setPositiveButton(getString(R.string.proceed)) { _, _ ->
        val newValue = binding.editField.text.toString()
        viewModel.updateMeters(this@PrbmDetailActivity, unit, newValue)
      }
      .show()
  }

  override fun onClickAzimuth(unit: PrbmUnit, value: Double) {
    val binding = EditNumericFieldBinding.inflate(layoutInflater)
    binding.editField.setText(value.toString())
    binding.editField.inputType = android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
    binding.textFieldTitle.hint = getString(R.string.azimuth_label)

    MaterialAlertDialogBuilder(this)
      .setTitle(getString(R.string.azimuth_explanation))
      .setView(binding.root)
      .setNegativeButton(getString(R.string.abort)) { _, _ -> }
      .setPositiveButton(getString(R.string.proceed)) { _, _ ->
        val newValue = binding.editField.text.toString()
        viewModel.updateAzimuth(this@PrbmDetailActivity, unit, newValue)
      }
      .show()
  }

  override fun onClickMinutes(unit: PrbmUnit, value: Int) {
    val binding = EditNumericFieldBinding.inflate(layoutInflater)
    binding.editField.setText(value.toString())
    binding.editField.inputType = android.text.InputType.TYPE_CLASS_NUMBER
    binding.textFieldTitle.hint = getString(R.string.minutes_label)

    MaterialAlertDialogBuilder(this)
      .setTitle(getString(R.string.minutes_explanation))
      .setView(binding.root)
      .setNegativeButton(getString(R.string.abort)) { _, _ -> }
      .setPositiveButton(getString(R.string.proceed)) { _, _ ->
        val newValue = binding.editField.text.toString()
        viewModel.updateMinutes(this@PrbmDetailActivity, unit, newValue)
      }
      .show()
  }

  private fun convertTimeStampToLocalDateTime(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
  }

  override fun onClickGps(unit: PrbmUnit) {
    MaterialAlertDialogBuilder(this)
      .setTitle(R.string.update_gps_coordinates)
      .setIcon(R.drawable.ic_gps_fixed)
      .setMessage(
        getString(
          R.string.coordinates_overview,
          unit.coordinates.longitude.toString(),
          unit.coordinates.latitude.toString(),
          unit.coordinates.bearing.toString(),
          unit.coordinates.speed.toString(),
          convertTimeStampToLocalDateTime(unit.coordinates.time)
        )
      )
      .setPositiveButton(getString(R.string.update_coordinates)) { _, _ ->
        viewModel.updateGpsCoordinatesForUnit(this@PrbmDetailActivity, unit)
      }
      .setNeutralButton(getString(R.string.abort)) { _, _ -> }
      .setNegativeButton(getString(R.string.remove_coordinates)) { _, _ ->
        viewModel.removeGpsCoordinatesForUnit(this@PrbmDetailActivity, unit)
      }
      .show()
  }

  override fun onAddUnitButtonClicked(unit: PrbmUnit) {
    viewModel.addUnitAbove(this@PrbmDetailActivity, unit)
  }

  override fun onClickDelete(unit: PrbmUnit) {
    MaterialAlertDialogBuilder(this)
      .setTitle(getString(R.string.delete_the_row))
      .setMessage(getString(R.string.are_you_sure_to_delete_a_row))
      .setNegativeButton(getString(R.string.abort)) { _, _ -> }
      .setPositiveButton(getString(R.string.ok)) { _, _ ->
        viewModel.deleteUnit(this@PrbmDetailActivity, unit)
      }
      .show()
  }

  override fun onNewEntityClicked(unit: PrbmUnit, columnIndex: Int, selectedEntityOptions: Int) {
    viewModel.addNewEntity(this, unit, columnIndex, selectedEntityOptions)
  }

  override fun onEntityClicked(unit: PrbmUnit, entity: PrbmEntity, columnIndex: Int) {
    viewModel.editEntity(this@PrbmDetailActivity, unit, entity, columnIndex)
  }
}
