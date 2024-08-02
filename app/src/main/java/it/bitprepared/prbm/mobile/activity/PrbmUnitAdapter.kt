package it.bitprepared.prbm.mobile.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.model.PrbmEntity
import it.bitprepared.prbm.mobile.model.PrbmUnit

class PrbmUnitAdapter(
    private val context: Context,
    resource: Int,
    objects: List<PrbmUnit>
) : ArrayAdapter<PrbmUnit>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
        getViewOptimize(position, convertView, parent)

    /**
     * Method to obtain current view in an optimized manner
     * @param position    Position in list
     * @param convertView ConvertView returned at each invocation
     * @param parent      Parent viewgroup
     * @return Just created/rendered view
     */
    fun getViewOptimize(position: Int, originalConvertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val unit = getItem(position)
        var convertView = originalConvertView

        if (convertView == null) {
            // Se la convertView e' nulla la devo caricare la prima volta

            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.list_units, parent, false)

            viewHolder = ViewHolder()
            viewHolder.txtAzimut = convertView.findViewById<View>(R.id.txtAzimut) as TextView
            viewHolder.txtMeters = convertView.findViewById<View>(R.id.txtMeters) as TextView
            viewHolder.txtMinutes = convertView.findViewById<View>(R.id.txtMinutes) as TextView
            viewHolder.txtGPS = convertView.findViewById<View>(R.id.txtGPS) as TextView
            viewHolder.lstFarLeft =
                convertView.findViewById<View>(R.id.lstEntityFarLeft) as LinearLayout
            viewHolder.lstFarRight =
                convertView.findViewById<View>(R.id.lstEntityFarRight) as LinearLayout
            viewHolder.lstNearLeft =
                convertView.findViewById<View>(R.id.lstEntityNearLeft) as LinearLayout
            viewHolder.lstNearRight =
                convertView.findViewById<View>(R.id.lstEntityNearRight) as LinearLayout
            viewHolder.btnFarLeft = convertView.findViewById<View>(R.id.btnAddFarLeft) as Button
            viewHolder.btnFarRight = convertView.findViewById<View>(R.id.btnAddFarRight) as Button
            viewHolder.btnNearLeft = convertView.findViewById<View>(R.id.btnAddNearLeft) as Button
            viewHolder.btnNearRight = convertView.findViewById<View>(R.id.btnAddNearRight) as Button

            // Set the tag tag
            convertView.tag = viewHolder
        } else {
            // Restore tag
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.txtAzimut!!.text = context.getString(R.string.azimut) + unit!!.azimuth
        viewHolder.txtMeters!!.text = context.getString(R.string.meters) + unit.meter
        viewHolder.txtMinutes!!.text = context.getString(R.string.minutes) + unit.minutes

        if (unit.isFlagAcquiringGPS) {
            viewHolder.txtGPS!!.text = "Coordinate: Acquisizione..."
            viewHolder.txtGPS!!.setTextColor(context.resources.getColor(R.color.black))
        } else if (unit.latitude == 0.0 && unit.longitude == 0.0) {
            viewHolder.txtGPS!!.text = "Coordinate: Assenti"
            viewHolder.txtGPS!!.setTextColor(context.resources.getColor(R.color.red))
        } else {
            viewHolder.txtGPS!!.text = "Coordinate: " + unit.latitude + " - " + unit.longitude
            viewHolder.txtGPS!!.setTextColor(context.resources.getColor(R.color.green))
        }
        // Setting onclick listner for add buttons
        val arrayBtn =
            arrayOf(
                viewHolder.btnFarLeft, viewHolder.btnNearLeft, viewHolder.btnNearRight,
                viewHolder.btnFarRight
            )
        for (j in arrayBtn.indices) {
            val column = j
            arrayBtn[j]!!.setOnClickListener { v: View? ->
                UserData.column = column
                UserData.unit = unit
                val addEntity = Intent(context, PrbmAddEntityActivity::class.java)
                (context as Activity).startActivityForResult(
                    addEntity,
                    PrbmDetailActivity.ACTIVITY_ADD_ENTITY
                )
            }
        }

        //        viewHolder.btnGPS.setBackgroundColor(c.getResources().getColor((unit.getLatitude() == 0) ? R.color.Red : R.color.LightGreen));
//        viewHolder.btnGPS.setOnClickListener(v -> {
//            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//            try {
//                ((Activity) c).startActivityForResult(builder.build((Activity) c), PrbmActivity.ACTIVITY_PLACE_PICKER);
//            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            }
//            if (unit.getLatitude() != 0 && unit.getLongitude() != 0) {
//                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(c);
//                builder.setTitle("Aggiornare coordinate GPS?");
//                builder.setMessage(
//                    "I dati GPS sono già stati acquisiti per questa osservazione, vuoi sovrascrivere?");
//                builder.setNegativeButton("No", (dialog, which) -> {
//                    dialog.dismiss();
//                });
//                builder.setPositiveButton("Si", (dialog, which) -> {
//                    requestGPSCoordinates(unit, v);
//                });
//                AlertDialog alert = builder.create();
//                alert.show();
//            } else {
//                requestGPSCoordinates(unit, v);
//            }
//        });
        val param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        param.setMargins(5, 0, 5, 5)

        // Array of entities and linear layouts
        val arrayEntity = arrayOf(
            unit.farLeft, unit.nearLeft, unit.nearRight, unit.farRight
        )
        val arrayLin = arrayOf(
            viewHolder.lstFarLeft,
            viewHolder.lstNearLeft,
            viewHolder.lstNearRight,
            viewHolder.lstFarRight
        )


        for (i in arrayLin.indices) {
            val entities: List<PrbmEntity> = arrayEntity[i]
            var j = 0
            while (j < entities.size) {
                val entity = entities[j]
                val v = arrayLin[i]!!.getChildAt(j + 1)
                if (v != null && v is Button) {
                    if (v.text.toString().contentEquals(entity.type)) {
                        j++
                        continue  // Button is already present...skip to next
                    } else arrayLin[i]!!.removeViewAt(j + 1)
                }

                // If not present, create a new button
                val b = Button(context)
                b.text = entity.type
                b.layoutParams = param
                b.isFocusable = false
                b.setBackgroundResource(entity.idButtonImage)
                b.setTextColor(context.resources.getColor(R.color.white))
                val column = i
                b.setOnClickListener {
                    UserData.column = column
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
                            ) { dialog, whichButton ->
                                unit.deleteEntity(entity, column)
                                this@PrbmUnitAdapter.notifyDataSetChanged()
                                dialog.dismiss()
                            }
                            alert.setNegativeButton(
                                R.string.abort
                            ) { dialog, whichButton -> dialog.dismiss() }
                            dialog.dismiss()
                            alert.show()
                        } else if (items[item].contentEquals(context.getString(R.string.move_up))) {
                            // Moving entity up
                            unit.moveEntity(entity, column, false)
                            this@PrbmUnitAdapter.notifyDataSetChanged()
                            dialog.dismiss()
                        } else if (items[item].contentEquals(context.getString(R.string.move_down))) {
                            // Moving entity down
                            unit.moveEntity(entity, column, true)
                            this@PrbmUnitAdapter.notifyDataSetChanged()
                            dialog.dismiss()
                        }
                    }
                    val alert = builder.create()
                    alert.show()
                }
                arrayLin[i]!!.addView(b)
                j++
            }
            try {
                // Remove remaining views (if exceed)
                arrayLin[i]!!.removeViews(j + 1, arrayLin[i]!!.childCount - (j + 1))
            } catch (e: Exception) {

            }
        }

        return requireNotNull(convertView)
    }

    /**
     * Viewholder class used to improve performance
     */
    private inner class ViewHolder {
        /** Reference to Azimut label  */
        var txtAzimut: TextView? = null
        /** Reference to Meters label  */
        var txtMeters: TextView? = null
        /** Reference to Steps label  */
        var txtMinutes: TextView? = null
        /** Reference to far left list  */
        var lstFarLeft: LinearLayout? = null
        /** Reference to near left list  */
        var lstNearLeft: LinearLayout? = null
        /** Reference to near right list  */
        var lstNearRight: LinearLayout? = null
        /** Reference to far right list  */
        var lstFarRight: LinearLayout? = null
        /** Reference to far left button  */
        var btnFarLeft: Button? = null
        /** Reference to near left button  */
        var btnNearLeft: Button? = null
        /** Reference to near right button  */
        var btnNearRight: Button? = null
        /** Reference to far right button  */
        var btnFarRight: Button? = null
        /** Reference to Coordinates  */
        var txtGPS: TextView? = null
    }
}
