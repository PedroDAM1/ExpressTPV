package com.pedro.expresstpv.domain.model

import com.pedro.expresstpv.data.database.entities.TipoIvaEntity

data class TipoIva(
    val id : Int = 0,
    val nombre : String,
    val porcentaje : Double
)

