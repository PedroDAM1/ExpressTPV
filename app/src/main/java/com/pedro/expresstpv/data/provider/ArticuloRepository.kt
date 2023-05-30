package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.ArticuloDaoI
import com.pedro.expresstpv.data.database.entities.ArticuloEntity
import com.pedro.expresstpv.data.usecase.CategoriaUseCase
import com.pedro.expresstpv.data.usecase.TipoIvaUseCase
import com.pedro.expresstpv.domain.model.Articulo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticuloRepository @Inject constructor(
    private val articuloDao: ArticuloDaoI,
    private val categoriaUseCase: CategoriaUseCase,
    private val tipoIvaUseCase: TipoIvaUseCase
    ) : BaseRepository<Articulo, ArticuloEntity>(articuloDao){

    /* ARTICULO */
    private fun Articulo.toEntity() = ArticuloEntity(
        id = id,
        idCategoria = categoria.id,
        idIva = tipoIva.id,
        nombre = nombre,
        precio = precio
    )

    override suspend fun toEntity(domain: Articulo): ArticuloEntity {
        return domain.toEntity()
    }

    override suspend fun toDomain(entity: ArticuloEntity): Articulo {
        //Tratamos de obtener la categoria, sino lo conseguimos obtenemos la categoria basica
        val categoria = categoriaUseCase.getById(entity.idCategoria) ?: categoriaUseCase.getById(0)!!
        //Intentamos obtener el iva, sino existe obtenemos el iva basico
        val tipoIva = tipoIvaUseCase.getById(entity.idIva) ?: tipoIvaUseCase.getById(0)!!
        Log.d("GET ARTICULO", "Se ha mapeado el tipo de iva")
        val articulo = Articulo(
            id = entity.id,
            nombre = entity.nombre,
            precio = entity.precio,
            categoria = categoria,
            tipoIva = tipoIva
        )
        Log.d("GET ARTICULO", "El articulo se ha mapaeado desde la funcion toDomain: $articulo")
        return articulo
    }
}


