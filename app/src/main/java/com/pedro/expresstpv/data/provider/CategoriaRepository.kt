package com.pedro.expresstpv.data.provider

import androidx.lifecycle.viewmodel.viewModelFactory
import com.pedro.expresstpv.data.database.dao.CategoriaDao
import com.pedro.expresstpv.data.database.entities.CategoriaEntity
import com.pedro.expresstpv.domain.model.Categoria
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Dictionary
import javax.inject.Inject

class CategoriaRepository @Inject constructor(private val categoriaDao: CategoriaDao) {

    private var categoriasMap = mutableMapOf<Int, Categoria>()

    init {
        // Inicializar las categorias en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            val categorias = categoriaDao.getAll().map { it.toDomain() }
            categorias.forEach {
                categoriasMap[it.id] = it
            }
        }
    }

    fun getAllCategorias() : MutableMap<Int, Categoria>{
        // Si ya se cargaron las categorias, devolverlas
        return categoriasMap

    }

    fun getCategoriaById(id : Int) : Categoria? {
        //Si la categoria no existe devolveremos nulo
        if (!categoriasMap.containsKey(id)){
            return null
        } else{
            return categoriasMap[id]
        }
    }

}

/* CATEGORIA */


fun Categoria.toEntity() = CategoriaEntity(
    id = id,
    nombre = nombre,
    color = color
)

public fun CategoriaEntity.toDomain() = Categoria(
    id = id,
    nombre = nombre,
    color = color
)
