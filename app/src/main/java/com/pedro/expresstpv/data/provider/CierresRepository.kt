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

    private val _mapCierres : MutableMap<Int, Cierre> = mutableMapOf()
    private val _mapCierresEntity : MutableMap<Int, CierreEntity> = mutableMapOf()
    private val _mapTempEntity : MutableMap<Int, CierreEntity> = mutableMapOf()

    private val _cierreEntityFlow : Flow<List<CierreEntity>> = cierreDao.getAll()
    private val _cierreFlow : Flow<List<Cierre>> = _cierreEntityFlow
        .onEach {
            loadCache(it)
        }
        .map {
           mapCierres()
            _mapCierres.values.toList()
        }
        .flowOn(Dispatchers.IO)

    private suspend fun loadFlow(){
        _cierreFlow.first()
    }

    private fun loadCache(list: List<CierreEntity>){
        list.forEach {
            val entry = _mapCierresEntity[it.numCierre]
            if (entry == null || entry != it){
                _mapCierresEntity[it.numCierre] = it
                _mapTempEntity[it.numCierre] = it
            }
        }
    }

    private fun mapCierres(){
        _mapTempEntity.forEach{(key , value)->
            val cierre = value.toDomain()
            _mapCierres[key] = cierre
        }
    }

    fun getAllCierres() = _cierreFlow

    suspend fun getCierreByNumCierre(numCierre : Int) : Cierre?{
        loadFlow()
        return _mapCierres[numCierre]
    }

    private fun CierreEntity.toDomain() : Cierre{
        return Cierre(
            numCierre = this.numCierre,
            fecha = this.fecha
        )
    }

}

