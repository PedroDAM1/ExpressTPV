package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.TipoIvaDao
import com.pedro.expresstpv.data.database.entities.TipoIvaEntity
import com.pedro.expresstpv.domain.model.TipoIva
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TipoIvaRepository @Inject constructor(private val tipoIvaDao: TipoIvaDao){

    private val tipoIvaMap = mutableMapOf<Int, TipoIva>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val tipoIvaEntities = tipoIvaDao.getAll().map { it.toDomain() }
            tipoIvaEntities.forEach {
                tipoIvaMap[it.id] = it
            }
        }
    }


    fun getAllTipoIva() : MutableMap<Int, TipoIva> {
        return tipoIvaMap
    }


    fun getTipoIvaById(id : Int) : TipoIva?{
        if (!tipoIvaMap.containsKey(id)){
            return null
        } else {
            return tipoIvaMap[id]
        }

    }


}

/* TIPO IVA */

fun TipoIvaEntity.toDomain() = TipoIva(
    id = id,
    nombre = nombre,
    porcentaje = porcentaje
)

fun TipoIva.toEntity() = TipoIvaEntity(
    id = id,
    nombre = nombre,
    porcentaje = porcentaje
)