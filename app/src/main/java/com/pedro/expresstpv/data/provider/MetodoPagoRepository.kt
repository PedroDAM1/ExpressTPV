package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.MetodoPagoDao
import com.pedro.expresstpv.data.database.entities.MetodoPagoEntity
import com.pedro.expresstpv.domain.model.MetodoPago
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetodoPagoRepository @Inject constructor(private val metodoPagoDao: MetodoPagoDao ) {

    private val _metodoPagoEntityFlow : Flow<List<MetodoPagoEntity>> = metodoPagoDao.getAll().flowOn(Dispatchers.IO)
    private val _metodoPagoFlow : Flow<List<MetodoPago>> = _metodoPagoEntityFlow
        .map {
            it.map {entity ->
                entity.toDomain()
            }
        }
        .flowOn(Dispatchers.IO)

    fun getAllMetodoPago() = _metodoPagoFlow

    suspend fun getMetodoPagoById(id : Int) : MetodoPago? {
        return metodoPagoDao.getById(id)
            .map {
                it?.toDomain()
            }
            .flowOn(Dispatchers.IO)
            .first()
    }

    private fun MetodoPagoEntity.toDomain() : MetodoPago{
        return MetodoPago(
            id = this.id,
            nombre = this.nombre
        )
    }

}