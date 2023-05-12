package com.pedro.expresstpv.data.provider

import android.util.DisplayMetrics
import android.util.Log
import com.pedro.expresstpv.data.database.dao.CategoriaDao
import com.pedro.expresstpv.data.database.entities.CategoriaEntity
import com.pedro.expresstpv.domain.model.Categoria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CategoriaRepository @Inject constructor(private val categoriaDao: CategoriaDao) {


    private val _categoriaEntityFlow: Flow<List<CategoriaEntity>> = categoriaDao.getAll()

    val categoriaFlow: Flow<List<Categoria>> = _categoriaEntityFlow
        .map {
            it.map { categoria ->
                categoria.toDomain()
            }
        }

    suspend fun getCategoriaById(id : Int) : Categoria? {
        //Si la categoria no existe devolveremos nulo
        return categoriaDao.getById(id)
            .map {
                it?.toDomain()
            }
            .flowOn(Dispatchers.IO)
            .singleOrNull()
    }

    fun getCategoriaByIdFlow(id : Int): Flow<Categoria?>{
        return categoriaDao.getById(id).map {
            it?.toDomain()
        }
            .flowOn(Dispatchers.IO)
    }

    suspend fun insert(categoria : Categoria){
        categoriaDao.insert(categoria.toEntity())
        Log.d("INSERT","Insert: $categoria")
    }



}

/* CATEGORIA */


fun Categoria.toEntity() = CategoriaEntity(
    id = id,
    nombre = nombre,
    color = color
)
fun CategoriaEntity.toDomain() = Categoria(
    id = id,
    nombre = nombre,
    color = color
)


