package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.ArticuloRepository
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.domain.model.TipoIva
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticulosUseCase @Inject constructor(
    private val articuloRepository: ArticuloRepository
) : BaseUseCase<Articulo>(articuloRepository) {

    private val _listaArticulosFlow = articuloRepository.getAllFlow()

    suspend fun getArticulosByName(nombre : String) : List<Articulo> = withContext(Dispatchers.IO){
        var lista : List<Articulo> = listOf()
        _listaArticulosFlow.collect{
            lista = it.filter { articulo ->
                articulo.nombre == nombre
            }
        }
        return@withContext lista
    }

    suspend fun getArticulosByCategoria(categoria: Categoria) : List<Articulo> = withContext(Dispatchers.Default){
        return@withContext this@ArticulosUseCase.getAll().filter {
            it.categoria == categoria
        }
    }

    fun getAllArticulosFlow() = articuloRepository.getAllFlow()

    suspend fun insertArticulo(nombre : String, precio : Double, categoria : Categoria, tipoIva : TipoIva){
        val articulo = Articulo(nombre = nombre, precio = precio, categoria = categoria, tipoIva = tipoIva)
        articuloRepository.insert(articulo)
    }

    suspend fun updateArticulo(id: Int, nombre : String, precio : Double, categoria: Categoria, tipoIva: TipoIva){
        val articulo = Articulo(id = id, nombre = nombre, precio = precio, categoria = categoria, tipoIva = tipoIva)
        articuloRepository.update(articulo)
    }


}