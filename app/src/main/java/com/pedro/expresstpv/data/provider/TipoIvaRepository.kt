package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.TipoIvaDaoI
import com.pedro.expresstpv.data.database.entities.TipoIvaEntity
import com.pedro.expresstpv.domain.model.TipoIva
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TipoIvaRepository @Inject constructor(
    private val tipoIvaDao: TipoIvaDaoI
) : BaseRepository<TipoIva, TipoIvaEntity>(tipoIvaDao){

    override suspend fun toDomain(entity: TipoIvaEntity): TipoIva {
        return entity.toDomain()
    }

    override suspend fun toEntity(domain: TipoIva): TipoIvaEntity {
        return domain.toEntity()
    }

    fun getTipoIvaByIdFlow(id: Int) : Flow<TipoIva?>{
        return tipoIvaDao.getById(id)
            .map {
                it?.toDomain()
            }
            .flowOn(Dispatchers.IO)
    }

    /* TIPO IVA */

    private fun TipoIvaEntity.toDomain() = TipoIva(
        id = id,
        nombre = nombre,
        porcentaje = porcentaje
    )

    private fun TipoIva.toEntity() = TipoIvaEntity(
        id = id,
        nombre = nombre,
        porcentaje = porcentaje
    )

}

