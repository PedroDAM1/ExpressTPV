package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.CategoriaRepository
import com.pedro.expresstpv.domain.model.Categoria
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriaUseCase @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) {

    suspend fun getAllCategoriasFlow() = categoriaRepository.getAllCategoriasFlow()

    suspend fun getAllCategorias() = categoriaRepository.getAllCategorias()

    suspend fun getCategoriaById(id : Int) = categoriaRepository.getCategoriaById(id)

    suspend fun insertCategoria(nombre : String, color : String){
        val categoria = Categoria(nombre = nombre, color = color)

        categoriaRepository.insert(categoria)

    }

}