package com.pedro.expresstpv.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedro.expresstpv.domain.model.TipoIva

@Entity(tableName = "tb_tipoiva")
data class TipoIvaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val porcentaje: Double
)
