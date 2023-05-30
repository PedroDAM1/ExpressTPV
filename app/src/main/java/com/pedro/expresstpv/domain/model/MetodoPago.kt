package com.pedro.expresstpv.domain.model

import com.pedro.expresstpv.data.database.entities.MetodoPagoEntity

data class MetodoPago(
    override val id : Int = 0,
    val nombre : String
) : IBaseModel
