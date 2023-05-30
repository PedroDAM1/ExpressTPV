package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.MetodoPagoDao
import com.pedro.expresstpv.data.database.entities.CierreEntity
import com.pedro.expresstpv.data.database.entities.MetodoPagoEntity
import com.pedro.expresstpv.data.usecase.BaseUseCase
import com.pedro.expresstpv.domain.model.MetodoPago
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetodoPagoRepository @Inject constructor(
    private val metodoPagoDao: MetodoPagoDao
) : BaseRepository<MetodoPago, MetodoPagoEntity>(metodoPagoDao) {

    override suspend fun toDomain(entity: MetodoPagoEntity): MetodoPago {
        return entity.toDomain()
    }

    override suspend fun toEntity(domain: MetodoPago): MetodoPagoEntity {
        return domain.toEntity()
    }


    private fun MetodoPagoEntity.toDomain() : MetodoPago{
        return MetodoPago(
            id = this.id,
            nombre = this.nombre
        )
    }

    private fun MetodoPago.toEntity() : MetodoPagoEntity {
        return MetodoPagoEntity(
            id = this.id,
            nombre = this.nombre
        )
    }

}