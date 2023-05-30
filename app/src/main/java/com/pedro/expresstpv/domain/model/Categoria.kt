package com.pedro.expresstpv.domain.model

data class Categoria(
    override val id: Int = 0,
    val nombre : String,
    val color : String = "#FFFFFF"
) : IBaseModel {
    override fun toString(): String {
        return nombre
    }
}