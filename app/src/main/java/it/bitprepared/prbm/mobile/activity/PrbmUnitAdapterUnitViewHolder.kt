package it.bitprepared.prbm.mobile.activity

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.databinding.ListUnitsBinding
import it.bitprepared.prbm.mobile.model.PrbmUnit

class PrbmUnitAdapterUnitViewHolder(
  private val b: ListUnitsBinding
) : RecyclerView.ViewHolder(b.root) {

  private var selectedEntityOptions = 0

  @SuppressLint("DiscouragedApi")
  fun bind(unit: PrbmUnit, listener: PrbmUnitAdapter.OnPrbmUnitListener) {
    val context = b.root.context
    b.lstFarLeft.removeAllViews()
    b.lstNearLeft.removeAllViews()
    b.lstNearRight.removeAllViews()
    b.lstFarRight.removeAllViews()
    b.chipMeters.text = context.getString(R.string.meters, unit.meters)
    b.chipAzimuth.text = context.getString(R.string.azimut, unit.azimuth)
    b.chipMinutes.text = context.getString(R.string.minutes, unit.minutes)
    b.chipMeters.setOnClickListener { listener.onClickMeters(unit, unit.meters) }
    b.chipAzimuth.setOnClickListener { listener.onClickAzimuth(unit, unit.azimuth) }
    b.chipMinutes.setOnClickListener { listener.onClickMinutes(unit, unit.minutes) }
    b.chipGps.setOnClickListener { listener.onClickGps(unit) }
    b.chipDelete.setOnClickListener { listener.onClickDelete(unit) }
    b.btnAddBelow.setOnClickListener { listener.onAddUnitButtonClicked(unit) }

    val color = if (unit.hasCoordinates()) R.color.green else R.color.red
    b.chipGps.setTextColor(ContextCompat.getColor(context, color))

    listOf(
      b.btnAddFarLeft, b.btnAddNearLeft, b.btnAddNearRight, b.btnAddFarRight
    ).forEachIndexed { index, button ->
      button.setOnClickListener {
        MaterialAlertDialogBuilder(context).setTitle(R.string.choose_entity)
          .setSingleChoiceItems(UserData.getSingleChoiceEntityType(), 0) { _, which ->
            selectedEntityOptions = which
          }.setNegativeButton(context.getString(R.string.abort)) { _, _ -> }
          .setPositiveButton(context.getString(R.string.proceed)) { _, _ ->
            listener.onNewEntityClicked(unit, index, selectedEntityOptions)
          }.show()
      }
    }

    mapOf(
      unit.farLeft to b.lstFarLeft,
      unit.nearLeft to b.lstNearLeft,
      unit.nearRight to b.lstNearRight,
      unit.farRight to b.lstFarRight
    ).forEach { (entities, layout) ->
      entities.forEach { currentEntity ->
        val unitButton = MaterialButton(
          context, null, com.google.android.material.R.attr.materialIconButtonOutlinedStyle
        )
        val drawableResourceId: Int =
          context.resources.getIdentifier(currentEntity.type.icon_name, "drawable", context.packageName)
        unitButton.icon = ContextCompat.getDrawable(context, drawableResourceId)
        val columnIndex = when(entities) {
          unit.farLeft -> 0
          unit.nearLeft -> 1
          unit.nearRight -> 2
          unit.farRight -> 3
          else -> error("Invalid index")
        }
        unitButton.setOnClickListener {
          listener.onEntityClicked(unit, currentEntity, columnIndex)
        }
        layout.addView(unitButton)
      }
    }
  }
}
