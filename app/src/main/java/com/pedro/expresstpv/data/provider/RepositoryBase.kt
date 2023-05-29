package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.IBaseDao
import com.pedro.expresstpv.data.database.entities.IBaseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

abstract class RepositoryBase <Domain, Entity : IBaseEntity> (
    private val dao : IBaseDao<Entity>
) : IBaseRepository<Domain> {

    private var _mapDomain : MutableMap<Int, Domain> = mutableMapOf()
    private var _mapEntity : MutableMap<Int, Entity> = mutableMapOf()
    private var _mapTempEntity : MutableMap<Int, Entity> = mutableMapOf()

    private val _entityFlow: Flow<List<Entity>> = dao.getAll()
    private val _domainFlow : Flow<List<Domain>> = _entityFlow
        .onEach {
            loadCache(it)
        }
        .map {
            mapDomain()
            _mapDomain.values.toList()
        }
        .flowOn(Dispatchers.IO)


    private suspend fun loadCache(list: List<Entity>){
        list.forEach {
            val entry = _mapEntity[it.id]
            if (entry == null || entry != it){
                _mapEntity[it.id] = it
                _mapTempEntity[it.id] = it
            }
        }
    }

    private suspend fun mapDomain() = withContext(Dispatchers.IO){
        _mapTempEntity.forEach{(key, value) ->
            val domain = toDomain(value)
            _mapDomain[key] = domain
        }
    }

    private suspend fun loadFlow(){
       _domainFlow.first()
    }

    override fun getAllFlow() = _domainFlow

    override suspend fun getAll() : List<Domain> {
        loadFlow()
        return _mapDomain.values.toList()
    }

    override suspend fun getById(id : Int) : Domain? {
        loadFlow()
        return _mapDomain[id]
    }

    abstract suspend fun toEntity(domain: Domain) : Entity

    abstract suspend fun toDomain(entity: Entity) : Domain
}