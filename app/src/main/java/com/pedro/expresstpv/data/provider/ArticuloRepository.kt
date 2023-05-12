package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.ArticuloDao
import com.pedro.expresstpv.data.database.entities.ArticuloConCategoriaETipoIva
import com.pedro.expresstpv.data.database.entities.ArticuloEntity
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.domain.model.TipoIva
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticuloRepository @Inject constructor(private val articuloDao: ArticuloDao, private val categoriaRepository: CategoriaRepository, private val tipoIvaRepository: TipoIvaRepository){

    /*private val _articulosEntityFlow : Flow<List<ArticuloEntity>> = articuloDao.getAll()
    private val articuloFlow : Flow<List<Articulo>> = _articulosEntityFlow
        .catch {
            Log.d("ERROR", "Error al mapear en el repositorio de articulos: ${it.message}")
        }
        .map {
            it.map { articulo ->
                articuloToDomain(articulo)
            }
        }
        .flowOn(Dispatchers.IO)

    fun getAllArticulos() = articuloFlow

    private suspend fun articuloToDomain(art : ArticuloEntity): Articulo{
        var categoria :Categoria? = null

        categoriaRepository.getCategoriaByIdFlow(art.id)
            .collect{
                categoria = it ?: categoriaRepository.getCategoriaById(0)!!
            }

        var tipoIva : TipoIva? = null

        tipoIvaRepository.getTipoIvaByIdFlow(art.id)
            .collect {
                tipoIva = it ?: tipoIvaRepository.getTipoIvaById(0)!!
            }

        return Articulo(
            id = art.id,
            nombre = art.nombre,
            precio = art.precio,
            categoria = categoria!!,
            tipoIva = tipoIva!!
        )
    }


    suspend fun insertarArticulo(articulo : Articulo){
        articuloDao.insert(articulo.toEntity())
    }*/
}

/* ARTICULO */
fun Articulo.toEntity() = ArticuloEntity(
    id = id,
    idCategoria = categoria.id,
    idIva = tipoIva.id,
    nombre = nombre,
    precio = precio
)
