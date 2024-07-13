package it.bitprepared.prbm.mobile.model

data class EntityField @JvmOverloads constructor(
    @JvmField val title: String,
    @JvmField val hint: String,
    @field:Transient val iD: Int,
    @JvmField val type: EntityFieldType,
    var value: String? = null,
)
