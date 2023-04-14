package com.pedro.expresstpv.domain.model

import com.pedro.expresstpv.data.database.dao.ArticuloDao
import com.pedro.expresstpv.data.database.dao.CategoriaDao
import com.pedro.expresstpv.data.database.dao.TipoIvaDao
import com.pedro.expresstpv.data.database.entities.ArticuloConCategoriaETipoIva
import com.pedro.expresstpv.data.database.entities.ArticuloEntity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

data class Articulo(
    val id: Int = 0,
    val categoria : Categoria,
    val tipoIva : TipoIva,
    val nombre : String,
    val precio : Double = 0.0
)

