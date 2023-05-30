package com.pedro.expresstpv.data.provider

import android.util.Log
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

//    private var _mapTipoIvaEntity : MutableMap<Int, TipoIvaEntity> = mutableMapOf()
//    private var _mapTempEntity : MutableMap<Int, TipoIvaEntity> = mutableMapOf()
//    private var _mapTipoIva : MutableMap<Int, TipoIva> = mutableMapOf()
//
//    private val _tipoIvaEntityFlow : Flow<List<TipoIvaEntity>> = tipoIvaDao.getAll()
//    private val _tipoIvaFlow : Flow<List<TipoIva>> = _tipoIvaEntityFlow
//        .onEach {
//            loadCache(it)
//        }
//        .map {
//            mapTipoIva()
//            _mapTipoIva.values.toList()
//            }
//        .flowOn(Dispatchers.IO)
//
//    private suspend fun loadFlow(){
//        _tipoIvaFlow.first()
//    }

    override suspend fun toDomain(entity: TipoIvaEntity): TipoIva {
        return entity.toDomain()
    }

    override suspend fun toEntity(domain: TipoIva): TipoIvaEntity {
        return domain.toEntity()
    }

    fun getTipoIvaByIdFlow(id: Int) : Flow<TipoIva?>{
        Log.d("GET TIPOIVA", "Obteniendo el flow del iva con id $id")
        return tipoIvaDao.getById(id)
            .map {
                Log.d("GET TIPOIVA", "Obteniendo un flow desde getbyid del tipoiva: $it")
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

