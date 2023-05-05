package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.ArticuloDao
import com.pedro.expresstpv.data.database.entities.ArticuloConCategoriaETipoIva
import com.pedro.expresstpv.data.database.entities.ArticuloEntity
import com.pedro.expresstpv.domain.extensions.Extensions
import com.pedro.expresstpv.domain.extensions.*
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.Categoria
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArticuloRepository @Inject constructor(private val articuloDao: ArticuloDao, private val categoriaRepository: CategoriaRepository, private val tipoIvaRepository: TipoIvaRepository){

    private val articulosMap = mutableMapOf<Int, Articulo>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val articulosEntities = articuloDao.getAll()
            //vamos a mapear cada entidad a su correspondiente de la clase de dominio trallendonos la informacion que neceitemos de los otros repositorios
            articulosEntities.forEach {
                val categoria = categoriaRepository.getCategoriaById(it.idCategoria)
                val tipoIva = tipoIvaRepository.getTipoIvaById(it.idIva)
                //Si el iva o la categoria no existen, saltaremos este articulo para evitar excepciones
                if (categoria == null || tipoIva == null) {
                    return@forEach
                }
                val articulo = Articulo(
                    id = it.id,
                    categoria = categoria,
                    tipoIva = tipoIva,
                    nombre = it.nombre,
                    precio = it.precio
                )

                articulosMap[it.id] = articulo
            }
        }
    }

    fun getAllArticulos() : MutableMap<Int, Articulo>{
        return articulosMap
    }
}

/* ARTICULO */


public fun ArticuloConCategoriaETipoIva.toDomain() = Articulo(
    id = articuloEntity.id,
    categoria = categoriaEntity.toDomain(),
    tipoIva = tipoIvaEntity.toDomain(),
    nombre = articuloEntity.nombre,
    precio = articuloEntity.precio
)

public fun Articulo.toEntity() = ArticuloEntity(
    id = id,
    idCategoria = categoria.id,
    idIva = tipoIva.id,
    nombre = nombre,
    precio = precio
)
