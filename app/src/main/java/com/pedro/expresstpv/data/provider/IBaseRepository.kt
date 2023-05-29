package com.pedro.expresstpv.data.provider

import kotlinx.coroutines.flow.Flow

interface IBaseRepository <Domain> {
    fun getAllFlow() : Flow<List<Domain>>

    suspend fun getAll() : List<Domain>

    suspend fun getById(id: Int) : Domain?

}