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

    private val _articulosEntityFlow : Flow<List<ArticuloEntity>> = articuloDao.getAll()
    private val articuloFlow : Flow<List<Articulo>> = _articulosEntityFlow
        .catch {
            Log.d("ERROR", "Error al mapear en el repositorio de articulos: ${it.message}")
        }
        .map {
            it.map { articulo ->
                Log.d("GET ARTICULO", "Mapeando articulo: $articulo")
                articuloToDomain(articulo)
            }
        }
        .flowOn(Dispatchers.IO)

    fun getAllArticulos() = articuloFlow

    private suspend fun articuloToDomain(art : ArticuloEntity): Articulo{
        var categoria :Categoria? = null
        categoria = categoriaRepository.getCategoriaById(art.idCategoria)
        /*categoriaRepository.getCategoriaByIdFlow(art.id)
            .catch {
                Log.d("GET ARTICULO", "Hubo un error al intentar obtener la categoria: ${it.message}")
            }
            .flowOn(Dispatchers.IO)
            .collect{
                categoria = it ?: categoriaRepository.getCategoriaById(0)!!
                Log.d("GET ARTICULO", "Mapeando la categoria: ${categoria?.id} ${categoria?.nombre} ${categoria?.color}")
            }
        Log.d("GET ARTICULO", "Se ha mapeado la categoria")*/
        var tipoIva : TipoIva? = null
        tipoIva = tipoIvaRepository.getTipoIvaById(art.idIva)
        /*tipoIvaRepository.getTipoIvaByIdFlow(art.id)
            .catch {
                Log.d("GET ARTICULO", "Hubo un error al intentar obtener el tipo iva: ${it.message}")
            }
            .flowOn(Dispatchers.IO)
            .collect {
                tipoIva = it ?: tipoIvaRepository.getTipoIvaById(0)!!
                Log.d("GET ARTICULO", "Mapeando el tipo de iva: ${tipoIva?.id} ${tipoIva?.nombre} ${tipoIva?.porcentaje}")
            }*/
        Log.d("GET ARTICULO", "Se ha mapeado el tipo de iva")
        val articulo = Articulo(
            id = art.id,
            nombre = art.nombre,
            precio = art.precio,
            categoria = categoria!!,
            tipoIva = tipoIva!!
        )
        Log.d("GET ARTICULO", "El articulo se ha mapaeado desde la funcion toDomain: $articulo")

        return articulo
    }


    suspend fun insertarArticulo(articulo : Articulo){
        Log.d("INSERTANDO ARTICULO", "Se esta insertando el articulo $articulo")
        articuloDao.insert(articulo.toEntity())
    }
}

/* ARTICULO */
fun Articulo.toEntity() = ArticuloEntity(
    id = id,
    idCategoria = categoria.id,
    idIva = tipoIva.id,
    nombre = nombre,
    precio = precio
)
