package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.TipoIvaDao
import com.pedro.expresstpv.data.database.entities.CategoriaEntity
import com.pedro.expresstpv.data.database.entities.TipoIvaEntity
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.domain.model.TipoIva
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TipoIvaRepository @Inject constructor(private val tipoIvaDao: TipoIvaDao){

    private val _tipoIvaEntityFlow : Flow<List<TipoIvaEntity>> = tipoIvaDao.getAll()

    val tipoIvaFlow : Flow<List<TipoIva>> = _tipoIvaEntityFlow
        .map {
            it.map {tipoIva ->
                tipoIva.toDomain()
            }
        }

/*
    suspend fun getTipoIvaById(id : Int) : TipoIva?{
        return tipoIvaDao.getById(id)
            .map { it?.toDomain() }
            .flowOn(Dispatchers.IO)
    }
*/

    fun getTipoIvaByIdFlow(id: Int) : Flow<TipoIva?>{
        return tipoIvaDao.getById(id)
            .map { it?.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    suspend fun insert(tipoIva: TipoIva){
        tipoIvaDao.insert(tipoIva = tipoIva.toEntity())
        Log.d("INSERT","Insert: $tipoIva")
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