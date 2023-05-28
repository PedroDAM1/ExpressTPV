package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.CierreDao
import com.pedro.expresstpv.data.database.entities.CierreEntity
import com.pedro.expresstpv.domain.model.Cierre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CierresRepository @Inject constructor(private val cierreDao: CierreDao) {

    private val _cierreEntityFlow : Flow<List<CierreEntity>> = cierreDao.getAll()
    private val _cierreFlow : Flow<List<Cierre>> = _cierreEntityFlow
        .map {
            it.map { entity ->
                Log.d("GET CIERRES", "Mapeando el cierre: $entity")
                entity.toDomain()
            }
        }
        .flowOn(Dispatchers.IO)

    fun getAllCierres() = _cierreFlow

    suspend fun getCierreByNumCierre(numCierre : Int) : Cierre?{
        return cierreDao.getByNumCierre(numCierre)
            .map {
                it?.toDomain()
            }
            .flowOn(Dispatchers.IO)
            .first()
    }

    private fun CierreEntity.toDomain() : Cierre{
        return Cierre(
            numCierre = this.numCierre,
            fecha = this.fecha
        )
    }

}

