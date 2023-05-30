package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.CategoriaDaoI
import com.pedro.expresstpv.data.database.entities.CategoriaEntity
import com.pedro.expresstpv.domain.model.Categoria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CategoriaRepository @Inject constructor(
    private val categoriaDao: CategoriaDaoI
) : BaseRepository<Categoria, CategoriaEntity>(categoriaDao) {

    override suspend fun toDomain(entity: CategoriaEntity): Categoria {
        return entity.toDomain()
    }

    override suspend fun toEntity(domain: Categoria): CategoriaEntity {
        return domain.toEntity()
    }

    fun getCategoriaByIdFlow(id : Int): Flow<Categoria?>{
        return categoriaDao.getById(id).map {
            Log.d("GET CATEGORIA", "Obteniendo flow de categoria por id: ${it?.id} ${it?.nombre}, ${it?.color}")
            it?.toDomain()
        }
            .flowOn(Dispatchers.IO)

    }

    /* CATEGORIA */


    private fun Categoria.toEntity() = CategoriaEntity(
        id = id,
        nombre = nombre,
        color = color
    )
    private fun CategoriaEntity.toDomain() = Categoria(
        id = id,
        nombre = nombre,
        color = color
    )


}



