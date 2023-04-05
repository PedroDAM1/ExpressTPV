package com.pedro.expresstpv.domain.model

data class Articulo(
    val id: Int = 0,
    val categoria : Categoria,
    val tipoIva : TipoIva,
    val nombre : String,
    val precio : Double = 0.0
)