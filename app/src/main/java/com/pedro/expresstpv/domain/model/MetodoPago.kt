package com.pedro.expresstpv.domain.model

data class MetodoPago(
    override val id : Int = 0,
    val nombre : String
) : IBaseModel
