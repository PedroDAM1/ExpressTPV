package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.CategoriaDaoI
import com.pedro.expresstpv.data.database.entities.CategoriaEntityI
import com.pedro.expresstpv.domain.model.Categoria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CategoriaRepository @Inject constructor(
    private val categoriaDao: CategoriaDaoI
) : RepositoryBase<Categoria, CategoriaEntityI>(categoriaDao) {


//    private val _categoriaEntityFlow: Flow<List<CategoriaEntity>> = categoriaDao.getAll()
//    private val _categoriaFlow : Flow<List<Categoria>> = _categoriaEntityFlow
//        .onEach {
//            loadCache(it)
//        }
//        .map {
//            mapCacheCategoria()
//            _mapaCategorias.values.toList()
//        }
//        .flowOn(Dispatchers.IO)
//
//
//    private var _mapaCategorias : MutableMap<Int, Categoria> = mutableMapOf()
//    private var _mapaCategoriaEntity : MutableMap<Int, CategoriaEntity> = mutableMapOf()
//    private var _mapaTempEntity : MutableMap<Int, CategoriaEntity> = mutableMapOf()

//    private suspend fun loadFlow(){
//        _categoriaFlow.first()
//    }

    override suspend fun toDomain(entity: CategoriaEntityI): Categoria {
        return entity.toDomain()
    }

    override suspend fun toEntity(domain: Categoria): CategoriaEntityI {
        return domain.toEntity()
    }

//    private fun loadCache(lista : List<CategoriaEntity>){
//        lista.forEach { entity ->
//            val categoriaEntry = _mapaCategoriaEntity[entity.id]
//            //Si la entity no existe o es diferente la cachearemos
//            if (categoriaEntry == null || categoriaEntry != entity) {
//                Log.d("CACHEO CATEGORIAS", "Se esta cacheando la categoria $entity")
//                _mapaCategoriaEntity[entity.id] = entity
//                //La cacheamos en el mapa temporal
//                _mapaTempEntity[entity.id] = entity
//            }
//        }
//    }
//
//    private fun mapCacheCategoria(){
//        // Vamos a recorrer el map temporal y mapearlo en un mapa de la clase de dominio
//        _mapaTempEntity.forEach{ (key, value) ->
//            val categoria = value.toDomain()
//            _mapaCategorias[key] = categoria
//        }
//        _mapaTempEntity.clear()
//    }

//    suspend fun getAllCategoriasFlow(): Flow<List<Categoria>> {
//        loadFlow()
//        return _categoriaFlow
//    }
//
//    suspend fun getAllCategorias() : List<Categoria> {
//        loadFlow()
//        return _mapaCategorias.values.toList()
//    }
//
//    suspend fun getCategoriaById(id : Int) : Categoria? {
//        loadFlow()
//        //Si la categoria no existe devolveremos nulo
//        return _mapaCategorias[id]
//    }

    fun getCategoriaByIdFlow(id : Int): Flow<Categoria?>{
        return categoriaDao.getById(id).map {
            Log.d("GET CATEGORIA", "Obteniendo flow de categoria por id: ${it?.id} ${it?.nombre}, ${it?.color}")
            it?.toDomain()
        }
            .flowOn(Dispatchers.IO)

    }

    suspend fun insert(categoria : Categoria){
        categoriaDao.insert(categoria.toEntity())
        Log.d("INSERT CATEGORIA","Insert: $categoria")
    }

    /* CATEGORIA */


    private fun Categoria.toEntity() = CategoriaEntityI(
        id = id,
        nombre = nombre,
        color = color
    )
    private fun CategoriaEntityI.toDomain() = Categoria(
        id = id,
        nombre = nombre,
        color = color
    )


}



