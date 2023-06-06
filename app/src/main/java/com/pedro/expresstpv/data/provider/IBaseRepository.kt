package com.pedro.expresstpv.data.provider

import kotlinx.coroutines.flow.Flow

interface IBaseRepository <Domain> {
    fun getAllFlow() : Flow<List<Domain>>

    suspend fun getAll() : List<Domain>

    suspend fun getById(id: Int) : Domain?

    suspend fun insert(domain: Domain)

    suspend fun insertAll(list: List<Domain>)

    suspend fun insertOrUpdate(domain: Domain)

    suspend fun update(domain: Domain)

    suspend fun updateAll(list: List<Domain>)

    suspend fun deleteList(list: List<Domain>)
    suspend fun delete(domain: Domain)
    suspend fun deleteAll()

}