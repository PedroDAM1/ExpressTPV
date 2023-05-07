package com.pedro.expresstpv.domain.model

data class Categoria(
    val id: Int = 0,
    val nombre : String,
    val color : String = "#FFFFFF"
) {
    override fun toString(): String {
        return nombre
    }
}