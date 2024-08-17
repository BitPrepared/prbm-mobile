package it.bitprepared.prbm.mobile.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import it.bitprepared.prbm.mobile.databinding.ListPrbmBinding
import it.bitprepared.prbm.mobile.model.Prbm

/**
 * Adapter used to render list of Prbms
 */
class ListPrbmAdapter(
    data: List<Prbm>,
    private val layoutInflater: LayoutInflater,
    private val listener: OnPrbmClickListener
) : RecyclerView.Adapter<ListPrbmAdapterViewHolder>() {

    private val _data: MutableList<Prbm> = data.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPrbmAdapterViewHolder {
        val binding = ListPrbmBinding.inflate(layoutInflater, parent, false)
        return ListPrbmAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListPrbmAdapterViewHolder, position: Int) {
        val prbm = _data[position]
        holder.bind(prbm, listener)
    }

    override fun getItemCount(): Int = _data.size

    fun setNewData(newData: List<Prbm>) {
        val diffCallback = DiffUtilCallback(_data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        _data.clear()
        _data.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
      // Remove DiffUtil from this Adapter
      notifyDataSetChanged()
    }


    interface OnPrbmClickListener {
        fun onEditPrbm(prbm: Prbm)
        fun onDeletePrbm(prbm: Prbm)
    }

    inner class DiffUtilCallback(
        private val oldData: MutableList<Prbm>, private val newData: List<Prbm>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldData.size

        override fun getNewListSize(): Int = newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldData[oldItemPosition].title == newData[newItemPosition].title

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldData[oldItemPosition].title == newData[newItemPosition].title &&
            oldData[oldItemPosition].place == newData[newItemPosition].place &&
            oldData[oldItemPosition].date == newData[newItemPosition].date &&
            oldData[oldItemPosition].authors == newData[newItemPosition].authors &&
              oldData[oldItemPosition].note == newData[newItemPosition].note
    }

}
