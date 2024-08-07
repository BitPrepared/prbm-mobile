package it.bitprepared.prbm.mobile.activity

import it.bitprepared.prbm.mobile.model.PrbmEntityField

data class EntityViewModelState(
  val isEditing: Boolean,
  val typeDescription: String,
  val time: String = "",
  val title: String = "",
  val description: String = "",
  val saveReady: Boolean = false,
  val fields: List<PrbmEntityField>,
  val fieldValues: Map<String, String>,
  val images: List<String>
)
