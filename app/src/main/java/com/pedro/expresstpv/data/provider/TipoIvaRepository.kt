package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.TipoIvaDao
import com.pedro.expresstpv.data.database.entities.TipoIvaEntity
import com.pedro.expresstpv.domain.model.TipoIva
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TipoIvaRepository @Inject constructor(private val tipoIvaDao: TipoIvaDao){

    private var _mapTipoIvaEntity : MutableMap<Int, TipoIvaEntity> = mutableMapOf()
    private var _mapTempEntity : MutableMap<Int, TipoIvaEntity> = mutableMapOf()
    private var _mapTipoIva : MutableMap<Int, TipoIva> = mutableMapOf()

    private val _tipoIvaEntityFlow : Flow<List<TipoIvaEntity>> = tipoIvaDao.getAll()
    private val _tipoIvaFlow : Flow<List<TipoIva>> = _tipoIvaEntityFlow
        .onEach {
            loadCache(it)
        }
        .map {
            mapTipoIva()
            _mapTipoIva.values.toList()
            }
        .flowOn(Dispatchers.IO)

    private suspend fun loadFlow(){
        _tipoIvaFlow.first()
    }
    private fun loadCache(lista : List<TipoIvaEntity>){
        lista.forEach {
            val entry = _mapTipoIvaEntity[it.id]
            if (entry == null || entry != it){
                _mapTipoIvaEntity[it.id] = it
                _mapTempEntity[it.id] = it
            }
        }
    }
    private fun mapTipoIva(){
        _mapTempEntity.forEach{ (key, value) ->
            val tipoIva = value.toDomain()
            _mapTipoIva[key] = tipoIva
        }
        _mapTempEntity.clear()
    }

    fun getAllTipoIva() = _tipoIvaFlow
    suspend fun getTipoIvaById(id : Int) : TipoIva? = withContext(Dispatchers.IO){
        loadFlow()
        return@withContext _mapTipoIva[id]
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

    suspend fun insert(tipoIva: TipoIva){
        tipoIvaDao.insert(tipoIva = tipoIva.toEntity())
        Log.d("INSERT","Insert: $tipoIva")
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

