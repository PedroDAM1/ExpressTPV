package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.CategoriaDao
import com.pedro.expresstpv.data.database.entities.CategoriaEntity
import com.pedro.expresstpv.domain.model.Categoria
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriaRepository @Inject constructor(private val categoriaDao: CategoriaDao) : IRepository {

    private var categoriasMap = mutableMapOf<Int, Categoria>()

    override suspend fun reloadFromDatabase() {
        val categorias = categoriaDao.getAll().map { it.toDomain() }
        categorias.forEach {
            categoriasMap[it.id] = it
        }
    }

    private suspend fun isMapVacio(){
        if (categoriasMap.isEmpty()){
            reloadFromDatabase()
        }
    }

    suspend fun getAllCategorias() : MutableMap<Int, Categoria>{
        isMapVacio()

        return categoriasMap
    }

    suspend fun getCategoriaById(id : Int) : Categoria? {
        isMapVacio()

        //Si la categoria no existe devolveremos nulo
        if (!categoriasMap.containsKey(id)){
            return null
        } else{
            return categoriasMap[id]
        }
    }

    suspend fun insert(categoria : Categoria){
        categoriaDao.insert(categoria.toEntity())
        /* Como no tenemos el id hasta que lo insertamos en la bd, lo insertaremos y luego haremos la consulta
        * Una vez que tengamos el id lo insertaremos en el mapa*/
        categoriasMap[categoriaDao.getLastId()] = categoria
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
