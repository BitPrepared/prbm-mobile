package it.bitprepared.prbm.mobile.activity

import androidx.recyclerview.widget.RecyclerView
import it.bitprepared.prbm.mobile.databinding.ListUnitsAddbuttonBinding

class PrbmUnitAdapterAddButtonViewHolder(
    private val b: ListUnitsAddbuttonBinding
) : RecyclerView.ViewHolder(b.root) {

    fun bind(listener: PrbmUnitAdapter.OnPrbmUnitListener, position: Int) {
        b.btnAddNewUnit.setOnClickListener {
            listener.onAddUnitButtonClicked(position)
        }
    }

}