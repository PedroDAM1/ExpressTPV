package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.ArticuloRepository
import com.pedro.expresstpv.domain.model.Articulo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticulosUseCase @Inject constructor(articuloRepository: ArticuloRepository) {

    private val _listaArticulosFlow = articuloRepository.getAllArticulos()

    suspend fun getArticulosByName(nombre : String) : List<Articulo> = withContext(Dispatchers.IO){
        var lista : List<Articulo> = listOf()
        _listaArticulosFlow.collect{
            lista = it.filter { articulo ->
                articulo.nombre == nombre
            }
        }
        return@withContext lista
    }

}