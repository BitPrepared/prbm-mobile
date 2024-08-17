package it.bitprepared.prbm.mobile.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import it.bitprepared.prbm.mobile.databinding.ListUnitsAddbuttonBinding
import it.bitprepared.prbm.mobile.databinding.ListUnitsBinding
import it.bitprepared.prbm.mobile.model.PrbmEntity
import it.bitprepared.prbm.mobile.model.PrbmUnit


class PrbmUnitAdapter(
  private val layoutInflater: LayoutInflater,
  private val listener: OnPrbmUnitListener
) : RecyclerView.Adapter<PrbmUnitAdapterUnitViewHolder>() {

  private var _data = mutableListOf<PrbmUnit>()

  fun setNewData(newData: List<PrbmUnit>) {
    _data = newData.toMutableList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrbmUnitAdapterUnitViewHolder =
    PrbmUnitAdapterUnitViewHolder(
      ListUnitsBinding.inflate(layoutInflater, parent, false)
    )


  override fun onBindViewHolder(holder: PrbmUnitAdapterUnitViewHolder, position: Int) {
    holder.bind(_data[position], listener)
  }

  override fun getItemCount(): Int = _data.size

  interface OnPrbmUnitListener {
    fun onClickMeters(unit: PrbmUnit, value: Int)
    fun onClickAzimuth(unit: PrbmUnit, value: Int)
    fun onClickMinutes(unit: PrbmUnit, value: Int)
    fun onClickGps(unit: PrbmUnit)
    fun onAddUnitButtonClicked(unit: PrbmUnit)
    fun onClickDelete(unit: PrbmUnit)
    fun onNewEntityClicked(unit: PrbmUnit, columnIndex: Int, selectedEntityOptions: Int)
    fun onEntityClicked(unit: PrbmUnit, entity: PrbmEntity, columnIndex: Int)
  }
}
