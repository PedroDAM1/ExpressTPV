package com.pedro.expresstpv.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_tipoiva")
data class TipoIvaEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    val nombre: String,
    val porcentaje: Double
) : IBaseEntity
