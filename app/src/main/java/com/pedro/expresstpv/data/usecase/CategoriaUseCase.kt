package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.CategoriaRepository
import com.pedro.expresstpv.domain.model.Categoria
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriaUseCase @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) : BaseUseCase<Categoria>(categoriaRepository) {

    fun getAllFlowWithId0() = categoriaRepository.getAllFlow()

    suspend fun insertCategoria(nombre : String, color : String){
        val categoria = Categoria(nombre = nombre, color = color)

        categoriaRepository.insert(categoria)
    }

    suspend fun updateCategoria(id: Int, nombre : String, color : String){
        val cat = Categoria(id, nombre, color)
        categoriaRepository.update(cat)
    }

}