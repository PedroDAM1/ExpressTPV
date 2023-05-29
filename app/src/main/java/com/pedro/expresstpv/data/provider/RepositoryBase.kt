package com.pedro.expresstpv.data.provider

abstract class RepositoryBase {

    suspend fun <Entity> loadCache(lista : List<Entity>, mapEntity : MutableMap<Int, Entity>, mapTemp : MutableMap<Int, Entity>, clazz : Class<Entity>){

    }

    suspend fun <Domain, Entity> mapEntityToDomain(mapDomain : MutableMap<Int, Domain>, mapTemp : MutableMap<Int, Entity>){

    }

}