package it.bitprepared.prbm.mobile.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.bitprepared.prbm.mobile.databinding.ListUnitsBinding
import it.bitprepared.prbm.mobile.model.PrbmUnit


class PrbmUnitAdapter(
    data: List<PrbmUnit>,
    private val layoutInflater: LayoutInflater,
) : RecyclerView.Adapter<PrbmUnitAdapterViewHolder>() {

    private val _data: MutableList<PrbmUnit> = data.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrbmUnitAdapterViewHolder =
        PrbmUnitAdapterViewHolder(this, ListUnitsBinding.inflate(layoutInflater, parent, false))

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: PrbmUnitAdapterViewHolder, position: Int) =
        holder.bind(_data[position])
}
