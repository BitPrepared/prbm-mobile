package it.bitprepared.prbm.mobile.model

data class NewPrbmEntityType(
    val id: String,
    val name: String,
    val description: String,
    val icon_color: String,
    val fields: List<EntityField>
)