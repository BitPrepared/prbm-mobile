package it.bitprepared.prbm.mobile.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.bitprepared.prbm.mobile.databinding.ListUnitsAddbuttonBinding
import it.bitprepared.prbm.mobile.databinding.ListUnitsBinding
import it.bitprepared.prbm.mobile.model.PrbmUnit


class PrbmUnitAdapter(
    private val data: List<PrbmUnit>,
    private val layoutInflater: LayoutInflater,
    private val listener: OnPrbmUnitListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == ViewType.ADD_UNIT.idx) {
            PrbmUnitAdapterAddButtonViewHolder(
                ListUnitsAddbuttonBinding.inflate(layoutInflater, parent, false)
            )
        } else {
            PrbmUnitAdapterUnitViewHolder(
                this, ListUnitsBinding.inflate(layoutInflater, parent, false)
            )
        }

    override fun getItemCount(): Int = (data.size * 2) + 1

    override fun getItemViewType(position: Int): Int =
        if (position % 2 == 0) ViewType.ADD_UNIT.ordinal else ViewType.UNIT.ordinal

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (holder.itemViewType == ViewType.ADD_UNIT.idx) {
            (holder as PrbmUnitAdapterAddButtonViewHolder).bind(listener, position)
        } else {
            (holder as PrbmUnitAdapterUnitViewHolder).bind(data[fromAdapterPositionToDataPosition(position)], listener)
        }

    interface OnPrbmUnitListener {
        fun onClickMeters(prbmUnit: PrbmUnit)
        fun onClickAzimuth(prbmUnit: PrbmUnit)
        fun onClickMinutes(prbmUnit: PrbmUnit)
        fun onClickGps(prbmUnit: PrbmUnit)
        fun onAddUnitButtonClicked(position: Int)
    }

    enum class ViewType(val idx: Int) {
        ADD_UNIT(0), UNIT(1)
    }

    fun fromAdapterPositionToDataPosition(adapterPosition: Int): Int = adapterPosition / 2
    fun fromDataPositionToAdapterPosition(dataPosition: Int): Int = (dataPosition * 2) + 1
}
