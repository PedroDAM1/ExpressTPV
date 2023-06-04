package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.IBaseDao
import com.pedro.expresstpv.data.database.entities.IBaseEntity
import com.pedro.expresstpv.domain.model.IBaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

abstract class BaseRepository <Domain: IBaseModel, Entity : IBaseEntity> (
    private val dao : IBaseDao<Entity>
) : IBaseRepository<Domain> {


    private var mapDomain : HashMap<Int, Domain> = hashMapOf()
    private var mapEntity : HashMap<Int, Entity> = hashMapOf()
    private var mapTempEntity : HashMap<Int, Entity> = hashMapOf()

    private val entityFlow: Flow<List<Entity>> = dao.getAll()
    private val domainFlow : Flow<List<Domain>> = entityFlow
        .onEach {
            loadCache(it)
        }
        .map {
            mapDomain()
            mapDomain.values.toList()
        }
        .flowOn(Dispatchers.IO)


    protected open suspend fun loadCache(list: List<Entity>){
        val mapEntityCopy = mapEntity.toMap()
        //Recorreremos la lista que nos llegue de la base de datos
        list.forEach {
            val entry = mapEntity[it.id]
            if (entry == null || entry != it){
                mapEntity[it.id] = it
                mapTempEntity[it.id] = it
            }
        }
        //Si por lo que sea la lista nos devuelve vacia deberemos de limpiar todos los mapas
        if (list.isEmpty()){
            clearMaps()
        }
        //Si el diccionario tiene elementos que no se encuentran en la lista, entonces deberemos de eliminar esos articulos concretos
        val filtrado = mapEntity.values.toList().filterNot {map ->
            list.contains(map)
        }
        filtrado.forEach {
            mapEntity.remove(it.id)
            mapTempEntity.remove(it.id)
            mapDomain.remove(it.id)
        }
    }

    private suspend fun mapDomain() = withContext(Dispatchers.Default){
//        val iterator = mapTempEntity.iterator()
//        while (iterator.hasNext()){
//            val domain = toDomain(iterator.next().value)
//            mapDomain[domain.id] = domain
//        }
        val mapTempEntityCopy = mapTempEntity.toMap()
        mapTempEntityCopy.forEach{ (key, value) ->
            val domain = toDomain(value)
            mapDomain[key] = domain
        }
        mapTempEntity.clear()
    }

    private fun clearMaps(){
        mapEntity.clear()
        mapTempEntity.clear()
        mapDomain.clear()
    }


    private suspend fun loadFlow(){
       domainFlow.first()
    }

    override fun getAllFlow() = domainFlow

    override suspend fun getAll() : List<Domain> {
        loadFlow()
        return mapDomain.values.toList()
    }

    override suspend fun getById(id : Int) : Domain? {
        loadFlow()
        return mapDomain[id]
    }

    override suspend fun insert(domain: Domain) {
        dao.insert(toEntity(domain))
    }

    override suspend fun insertAll(list: List<Domain>) {
        dao.insertAll(list.map { toEntity(it) })
    }

    override suspend fun update(domain: Domain) {
        dao.update(toEntity(domain))
    }

    override suspend fun updateAll(list: List<Domain>) {
        dao.updateAll(list.map { toEntity(it) })
    }

    override suspend fun delete(domain: Domain){
        dao.delete(toEntity(domain))
    }
    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun deleteList(list: List<Domain>) {
        dao.deleteList(list.map { toEntity(it) })
    }

    abstract suspend fun toEntity(domain: Domain) : Entity

    abstract suspend fun toDomain(entity: Entity) : Domain
}