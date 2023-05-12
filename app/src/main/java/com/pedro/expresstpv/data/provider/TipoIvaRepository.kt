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
            Log.d("GET TIPOIVA", "Mapeando el flow desde la base de datos")
            it.map {tipoIva ->
                Log.d("GET TIPOIVA", "Se esta mapeando el ${tipoIva.id}, ${tipoIva.nombre}, ${tipoIva.porcentaje}")
                tipoIva.toDomain()
            }
        }

    suspend fun getTipoIvaById(id : Int) : TipoIva?{
        return tipoIvaDao.getById(id)
            .catch {
                Log.d("GET TIPOIVA", "Error al intentar obtener el iva con id $id")
            }
            .map {
                Log.d("GET TIPOIVA", "Mapeando el tipoIva al dominio: $it")
                it?.toDomain()
            }
            .flowOn(Dispatchers.IO)
            .first()

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