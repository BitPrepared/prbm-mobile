package it.bitprepared.prbm.mobile.activity

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.databinding.ListUnitsBinding
import it.bitprepared.prbm.mobile.model.PrbmEntity
import it.bitprepared.prbm.mobile.model.PrbmUnit

class PrbmUnitAdapterViewHolder(private val adapter: PrbmUnitAdapter, private val b: ListUnitsBinding) :
    RecyclerView.ViewHolder(b.root) {

    fun bind(unit: PrbmUnit) {
        val context = b.root.context
        b.txtAzimuth.text = context.getString(R.string.azimut, unit.azimuth)
        b.txtMeters.text = context.getString(R.string.meters, unit.meter)
        b.txtMinutes.text = context.getString(R.string.minutes, unit.minutes)

        if (unit.isFlagAcquiringGPS) {
            b.txtGPS.text = context.getString(R.string.coordinates_acquiring)
            b.txtGPS.setTextColor(ContextCompat.getColor(context, R.color.black))
        } else if (unit.latitude == 0.0 && unit.longitude == 0.0) {
            b.txtGPS.text = b.txtMinutes.context.getString(R.string.coordinates_missing)
            b.txtGPS.setTextColor(ContextCompat.getColor(context, R.color.red))
        } else {
            b.txtGPS.text = context.getString(R.string.coordinates, unit.latitude, unit.longitude)
            b.txtGPS.setTextColor(ContextCompat.getColor(context, R.color.green))
        }
        listOf(
            b.btnAddFarLeft,
            b.btnAddNearLeft,
            b.btnAddNearRight,
            b.btnAddFarRight
        ).forEachIndexed { index, button ->
            button.setOnClickListener {
                UserData.column = index
                UserData.unit = unit
                val addEntity = Intent(button.context, PrbmAddEntityActivity::class.java)
                // TODO This will break.
                (context as Activity).startActivityForResult(
                    addEntity,
                    PrbmDetailActivity.ACTIVITY_ADD_ENTITY
                )
            }
        }
        val param = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        param.setMargins(5, 0, 5, 5)

        // Array of entities and linear layouts
        val arrayEntity = listOf(unit.farLeft, unit.nearLeft, unit.nearRight, unit.farRight)
        val arrayLin = listOf(b.lstEntityFarLeft, b.lstEntityNearLeft, b.lstEntityNearRight, b.lstEntityFarRight)

        for (i in arrayLin.indices) {
            val entities: List<PrbmEntity> = arrayEntity[i]
            var j = 0
            while (j < entities.size) {
                val entity = entities[j]
                val v = arrayLin[i].getChildAt(j + 1)
                if (v != null && v is Button) {
                    if (v.text.toString().contentEquals(entity.type)) {
                        j++
                        continue  // Button is already present...skip to next
                    } else arrayLin[i].removeViewAt(j + 1)
                }

                // If not present, create a new button
                val b = Button(context)
                b.text = entity.type
                b.layoutParams = param
                b.isFocusable = false
                b.setBackgroundResource(entity.idButtonImage)
                b.setTextColor(ContextCompat.getColor(context, R.color.white))
                b.setOnClickListener {
                    UserData.column = i
                    UserData.unit = unit
                    UserData.entity = entity

                    // Creating a contextual menu as Item Dialog
                    val menuItems = ArrayList<String>()
                    menuItems.add(context.getString(R.string.modify))
                    menuItems.add(context.getString(R.string.delete))

                    // Check if entity can move up or down
                    if (UserData.canMoveUnitUp()) {
                        menuItems.add(context.getString(R.string.move_up))
                    }
                    if (UserData.canMoveUnitDown()) {
                        menuItems.add(context.getString(R.string.move_down))
                    }

                    val items = menuItems.toTypedArray<String>()
                    val builder = MaterialAlertDialogBuilder(context)
                    builder.setTitle(context.getString(R.string.entity_menu))
                    builder.setItems(items) { dialog, item ->
                        // Handling item selection
                        if (item == 0) {
                            val addEntity = Intent(context, EntityActivity::class.java)
                            addEntity.putExtra("edit", true)
                            (context as Activity).startActivityForResult(
                                addEntity,
                                PrbmDetailActivity.ACTIVITY_MODIFY_ENTITY
                            )
                            dialog.dismiss()
                        } else if (item == 1) {
                            // Alert dialog to check if sure to delete

                            val alert = MaterialAlertDialogBuilder(context)
                            alert.setTitle(context.getString(R.string.confirm_delete))
                            alert.setMessage(context.getString(R.string.are_you_sure_delete_entity))
                            alert.setIcon(R.drawable.ic_alert_black_48dp)
                            alert.setPositiveButton(
                                R.string.delete
                            ) { d, _ ->
                                unit.deleteEntity(entity, i)
                                adapter.notifyDataSetChanged()
                                d.dismiss()
                            }
                            alert.setNegativeButton(
                                R.string.abort
                            ) { d, _ -> d.dismiss() }
                            dialog.dismiss()
                            alert.show()
                        } else if (items[item].contentEquals(context.getString(R.string.move_up))) {
                            // Moving entity up
                            unit.moveEntity(entity, i, false)
                            adapter.notifyDataSetChanged()
                            dialog.dismiss()
                        } else if (items[item].contentEquals(context.getString(R.string.move_down))) {
                            // Moving entity down
                            unit.moveEntity(entity, i, true)
                            adapter.notifyDataSetChanged()
                            dialog.dismiss()
                        }
                    }
                    val alert = builder.create()
                    alert.show()
                }
                arrayLin[i].addView(b)
                j++
            }
            try {
                // Remove remaining views (if exceed)
                arrayLin[i].removeViews(j + 1, arrayLin[i].childCount - (j + 1))
            } catch (_: Exception) {

            }
        }
    }

}