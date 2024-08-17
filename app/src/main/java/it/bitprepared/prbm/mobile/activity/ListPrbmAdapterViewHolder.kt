package it.bitprepared.prbm.mobile.activity

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import it.bitprepared.prbm.mobile.databinding.ListPrbmBinding
import it.bitprepared.prbm.mobile.model.Prbm

class ListPrbmAdapterViewHolder(private val binding: ListPrbmBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(prbm: Prbm, listener: ListPrbmAdapter.OnPrbmClickListener) {
        val stringBuilder = SpannableStringBuilder()
            .bold { append(prbm.title) }
        if (prbm.authors?.isNotBlank() == true) {
            stringBuilder.append("\n")
            stringBuilder.append(prbm.authors)
        }
        if (prbm.place?.isNotBlank() == true) {
            stringBuilder.append("\n")
            stringBuilder.append("${prbm.place} - ${prbm.date}".trim())
        }
        if (prbm.note?.isNotBlank() == true) {
            stringBuilder.append("\n")
            stringBuilder.append("Note: ${prbm.note}".trim())
        }
        binding.txtListContent.text = stringBuilder
        binding.btnEdit.setOnClickListener {
            listener.onEditPrbm(prbm)
        }
        binding.btnDelete.setOnClickListener {
            listener.onDeletePrbm(prbm)
        }
    }

}
