package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.ArticuloDao
import com.pedro.expresstpv.data.database.entities.ArticuloEntity
import com.pedro.expresstpv.domain.model.Articulo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticuloRepository @Inject constructor(private val articuloDao: ArticuloDao, private val categoriaRepository: CategoriaRepository, private val tipoIvaRepository: TipoIvaRepository){
    //Obtenemos el flow desde la base de datos
    private val _articulosEntityFlow : Flow<List<ArticuloEntity>> = articuloDao.getAll()
    //Mapeamos ese flow para convertirlo en uno que podamos usar en el resto de la aplicacion
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

    /**
     * Devuelve el flow mapeado a la capa de dominio con la lista de articulos de la base de datos
     */
    fun getAllArticulos() = articuloFlow

    private suspend fun articuloToDomain(art : ArticuloEntity): Articulo{
        //Tratamos de obtener la categoria, sino lo conseguimos obtenemos la categoria basica
        val categoria = categoriaRepository.getCategoriaById(art.idCategoria) ?: categoriaRepository.getCategoriaById(0)!!
        //Intentamos obtener el iva, sino existe obtenemos el iva basico
        val tipoIva = tipoIvaRepository.getTipoIvaById(art.idIva) ?: tipoIvaRepository.getTipoIvaById(0)!!
        Log.d("GET ARTICULO", "Se ha mapeado el tipo de iva")
        val articulo = Articulo(
            id = art.id,
            nombre = art.nombre,
            precio = art.precio,
            categoria = categoria,
            tipoIva = tipoIva
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
