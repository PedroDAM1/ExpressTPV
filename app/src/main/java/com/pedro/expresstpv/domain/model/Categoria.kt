package com.pedro.expresstpv.domain.model

import com.pedro.expresstpv.data.database.entities.CategoriaEntity

data class Categoria(
    val id: Int = 0,
    val nombre : String,
    val color : String = "#FFFFFF"
)