package com.pedro.expresstpv.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedro.expresstpv.data.provider.IBaseRepository
import com.pedro.expresstpv.domain.model.MetodoPago

@Entity(tableName = "tb_metodopago")
data class MetodoPagoEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    val nombre: String
) : IBaseEntity

