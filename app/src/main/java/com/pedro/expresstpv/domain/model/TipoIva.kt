package com.pedro.expresstpv.domain.model

data class TipoIva(
    override val id : Int = 0,
    val nombre : String,
    val porcentaje : Double
) : IBaseModel {
    override fun toString(): String {
        return nombre
    }
}

