package it.bitprepared.prbm.mobile.model

data class PrbmEntityType(
    val id: String,
    val name: String,
    val description: String,
    val icon_color: String,
    val fields: List<PrbmEntityField>
)