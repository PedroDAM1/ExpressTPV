package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.MetodoPagoDao
import com.pedro.expresstpv.data.database.entities.CierreEntity
import com.pedro.expresstpv.data.database.entities.MetodoPagoEntity
import com.pedro.expresstpv.domain.model.MetodoPago
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetodoPagoRepository @Inject constructor(private val metodoPagoDao: MetodoPagoDao ) {

    private var _mapMetodoPago : MutableMap<Int, MetodoPago> = mutableMapOf()
    private var _mapMetodoPagoEntity : MutableMap<Int, MetodoPagoEntity> = mutableMapOf()
    private var _mapTempEntity : MutableMap<Int, MetodoPagoEntity> = mutableMapOf()

    private val _metodoPagoEntityFlow : Flow<List<MetodoPagoEntity>> = metodoPagoDao.getAll().flowOn(Dispatchers.IO)
    private val _metodoPagoFlow : Flow<List<MetodoPago>> = _metodoPagoEntityFlow
        .onEach {
            loadCache(it)
        }
        .map {
            mapCierres()
            _mapMetodoPago.values.toList()
        }
        .flowOn(Dispatchers.IO)

    private suspend fun loadFlow(){
        _metodoPagoFlow.first()
    }

    private fun loadCache(list: List<MetodoPagoEntity>){
        list.forEach {
            val entry = _mapMetodoPagoEntity[it.id]
            if (entry == null || entry != it){
                _mapMetodoPagoEntity[it.id] = it
                _mapTempEntity[it.id] = it
            }
        }
    }

    private fun mapCierres(){
        _mapTempEntity.forEach{(key , value)->
            val cierre = value.toDomain()
            _mapMetodoPago[key] = cierre
        }
    }

    fun getAllMetodoPago() = _metodoPagoFlow

    suspend fun getMetodoPagoById(id : Int) : MetodoPago? {
        loadFlow()
        return _mapMetodoPago[id]
    }

    private fun MetodoPagoEntity.toDomain() : MetodoPago{
        return MetodoPago(
            id = this.id,
            nombre = this.nombre
        )
    }

}