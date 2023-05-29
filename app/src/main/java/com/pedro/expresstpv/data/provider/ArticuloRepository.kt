package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.ArticuloDao
import com.pedro.expresstpv.data.database.entities.ArticuloEntity
import com.pedro.expresstpv.data.usecase.CategoriaUseCase
import com.pedro.expresstpv.data.usecase.TipoIvaUseCase
import com.pedro.expresstpv.domain.model.Articulo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticuloRepository @Inject constructor(
    private val articuloDao: ArticuloDao,
    private val categoriaUseCase: CategoriaUseCase,
    private val tipoIvaUseCase: TipoIvaUseCase
    ){

    private var _mapArticulos : MutableMap<Int, Articulo> = mutableMapOf()
    private var _mapArticulosEntity : MutableMap<Int, ArticuloEntity> = mutableMapOf()
    private var _mapTempEntity : MutableMap<Int, ArticuloEntity> = mutableMapOf()

    //Obtenemos el flow desde la base de datos
    private val _articulosEntityFlow : Flow<List<ArticuloEntity>> = articuloDao.getAll()
    //Mapeamos ese flow para convertirlo en uno que podamos usar en el resto de la aplicacion
    private val articuloFlow : Flow<List<Articulo>> = _articulosEntityFlow
        .catch {
            Log.d("ERROR", "Error al mapear en el repositorio de articulos: ${it.message}")
        }
        .onEach {
            loadCache(it)
        }
        .map {
            mapArticulos()
            _mapArticulos.values.toList()

        }
        .flowOn(Dispatchers.IO)

    private suspend fun loadFlow(){
        articuloFlow.first()
    }

    private fun loadCache(lista: List<ArticuloEntity>){
        lista.forEach {
            val entry = _mapArticulosEntity[it.id]
            if(entry == null || entry != it){
                _mapArticulosEntity[it.id] = it
                _mapTempEntity[it.id] = it
            }
        }
    }

    private suspend fun mapArticulos(){
        _mapTempEntity.forEach{ (key, value) ->
            val articulo = value.toDomain()
            _mapArticulos[key] = articulo
        }
        _mapTempEntity.clear()
    }

    /**
     * Devuelve el flow mapeado a la capa de dominio con la lista de articulos de la base de datos
     */
    fun getAllArticulos() = articuloFlow

    private suspend fun ArticuloEntity.toDomain(): Articulo{
        //Tratamos de obtener la categoria, sino lo conseguimos obtenemos la categoria basica
        val categoria = categoriaUseCase.getCategoriaById(this.idCategoria) ?: categoriaUseCase.getCategoriaById(0)!!
        //Intentamos obtener el iva, sino existe obtenemos el iva basico
        val tipoIva = tipoIvaUseCase.getTipoIvaById(this.idIva) ?: tipoIvaUseCase.getTipoIvaById(0)!!
        Log.d("GET ARTICULO", "Se ha mapeado el tipo de iva")
        val articulo = Articulo(
            id = this.id,
            nombre = this.nombre,
            precio = this.precio,
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

    /* ARTICULO */
    private fun Articulo.toEntity() = ArticuloEntity(
        id = id,
        idCategoria = categoria.id,
        idIva = tipoIva.id,
        nombre = nombre,
        precio = precio
    )
}


