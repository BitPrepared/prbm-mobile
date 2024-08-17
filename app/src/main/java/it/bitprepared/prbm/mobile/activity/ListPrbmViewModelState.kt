package it.bitprepared.prbm.mobile.activity

import it.bitprepared.prbm.mobile.model.Prbm

data class ListPrbmViewModelState(
  val prbmList: List<Prbm> = UserData.prbmList,
  val prbmToEdit: Prbm? = null,
  var lastUpdated: Long = System.currentTimeMillis()
)
