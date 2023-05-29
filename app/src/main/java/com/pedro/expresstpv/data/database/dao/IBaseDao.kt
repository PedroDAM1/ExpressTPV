package com.pedro.expresstpv.data.database.dao

import kotlinx.coroutines.flow.Flow

interface IBaseDao<Entity> {

    abstract fun getAll() : Flow<List<Entity>>

}