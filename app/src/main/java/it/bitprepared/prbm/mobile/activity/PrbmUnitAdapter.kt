package it.bitprepared.prbm.mobile.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import it.bitprepared.prbm.mobile.databinding.ListUnitsAddbuttonBinding
import it.bitprepared.prbm.mobile.databinding.ListUnitsBinding
import it.bitprepared.prbm.mobile.model.PrbmUnit


class PrbmUnitAdapter(
    private val layoutInflater: LayoutInflater,
    private val listener: OnPrbmUnitListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _data = mutableListOf<PrbmUnitAdapterViewType>()

    fun setNewData(newData: List<PrbmUnit>) {
        System.err.println("setNewData")
        System.err.println("old $_data")
        val paddedNewData = padListWithAddButtons(newData)
        System.err.println("new $paddedNewData")
        val diffCallback = DiffUtilCallback(_data, paddedNewData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        _data = paddedNewData.map {
            if (it is PrbmUnitAdapterViewType.Unit) {
                PrbmUnitAdapterViewType.Unit(
                    it.prbmUnit.copy(
                        entitiesFarLeft = it.prbmUnit.entitiesFarLeft.toMutableList(),
                        entitiesFarRight = it.prbmUnit.entitiesFarRight.toMutableList(),
                        entitiesNearLeft = it.prbmUnit.entitiesNearLeft.toMutableList(),
                        entitiesNearRight = it.prbmUnit.entitiesNearRight.toMutableList()
                    )
                )
            } else {
                PrbmUnitAdapterViewType.AddButton
            }
        }.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == PrbmUnitAdapterViewType.AddButton.idx) {
            PrbmUnitAdapterAddButtonViewHolder(
                ListUnitsAddbuttonBinding.inflate(layoutInflater, parent, false)
            )
        } else {
            PrbmUnitAdapterUnitViewHolder(
                this, ListUnitsBinding.inflate(layoutInflater, parent, false)
            )
        }

    override fun getItemCount(): Int = _data.size

    override fun getItemViewType(position: Int): Int =
        if (position % 2 == 0) PrbmUnitAdapterViewType.AddButton.idx else PrbmUnitAdapterViewType.Unit.idx

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (holder.itemViewType == PrbmUnitAdapterViewType.AddButton.idx) {
            (holder as PrbmUnitAdapterAddButtonViewHolder).bind(listener, position)
        } else {
            val unit = _data[position] as PrbmUnitAdapterViewType.Unit
            (holder as PrbmUnitAdapterUnitViewHolder).bind(unit.prbmUnit, listener, position)
        }

    interface OnPrbmUnitListener {
        fun onClickMeters(value: Int, position: Int)
        fun onClickAzimuth(value: Int, position: Int)
        fun onClickMinutes(value: Int, position: Int)
        fun onClickGps(position: Int)
        fun onAddUnitButtonClicked(position: Int)
        fun onClickDelete(position: Int)
    }

    fun fromAdapterPositionToDataPosition(adapterPosition: Int): Int = adapterPosition / 2

    private fun padListWithAddButtons(input: List<PrbmUnit>): List<PrbmUnitAdapterViewType> {
        val paddedData = mutableListOf<PrbmUnitAdapterViewType>(PrbmUnitAdapterViewType.AddButton)
        input.forEach {
            paddedData.add(PrbmUnitAdapterViewType.Unit(it))
            paddedData.add(PrbmUnitAdapterViewType.AddButton)
        }
        return paddedData
    }

    inner class DiffUtilCallback(
        private val oldData: MutableList<PrbmUnitAdapterViewType>, private val newData: List<PrbmUnitAdapterViewType>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldData.size

        override fun getNewListSize(): Int = newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldData[oldItemPosition]
            val new = newData[newItemPosition]
            return if (old is PrbmUnitAdapterViewType.Unit && new is PrbmUnitAdapterViewType.Unit) {
                areContentsTheSame(oldItemPosition, newItemPosition)
            } else {
                old == new
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldData[oldItemPosition]
            val new = newData[newItemPosition]
            return if (old is PrbmUnitAdapterViewType.Unit && new is PrbmUnitAdapterViewType.Unit) {
                old.prbmUnit.meters == new.prbmUnit.meters &&
                    old.prbmUnit.azimuth == new.prbmUnit.azimuth &&
                    old.prbmUnit.minutes == new.prbmUnit.minutes &&
                    old.prbmUnit.entitiesFarLeft == new.prbmUnit.entitiesFarLeft &&
                    old.prbmUnit.entitiesFarRight == new.prbmUnit.entitiesFarRight &&
                    old.prbmUnit.entitiesNearLeft == new.prbmUnit.entitiesNearLeft &&
                    old.prbmUnit.entitiesNearRight == new.prbmUnit.entitiesNearRight
            } else {
                old == new
            }
        }
    }
}
