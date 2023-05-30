package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.CierreDao
import com.pedro.expresstpv.data.database.entities.CierreEntity
import com.pedro.expresstpv.domain.model.Cierre
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CierresRepository @Inject constructor(
    private val cierreDao: CierreDao
) : BaseRepository<Cierre, CierreEntity>(cierreDao) {

    override suspend fun toDomain(entity: CierreEntity): Cierre {
        return entity.toDomain()
    }

    override suspend fun toEntity(domain: Cierre): CierreEntity {
        return domain.toEntity()
    }

    private fun CierreEntity.toDomain() : Cierre{
        return Cierre(
            id = id,
            numCierre = this.numCierre,
            fecha = this.fecha
        )
    }

    private fun Cierre.toEntity() : CierreEntity{
        return CierreEntity(
            id = this.id,
            numCierre = this.numCierre,
            fecha = this.fecha
        )
    }

}

