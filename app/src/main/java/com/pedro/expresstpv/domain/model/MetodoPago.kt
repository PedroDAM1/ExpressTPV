package com.pedro.expresstpv.domain.model

import com.pedro.expresstpv.data.database.entities.MetodoPagoEntity

data class MetodoPago(
    val id : Int = 0,
    val nombre : String
)

fun MetodoPagoEntity.toDomain() = MetodoPago(
    id = id,
    nombre = nombre
)