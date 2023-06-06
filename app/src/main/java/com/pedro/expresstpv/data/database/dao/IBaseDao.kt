package com.pedro.expresstpv.data.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface IBaseDao<Entity> {

    fun getAll() : Flow<List<Entity>>

    @Insert
    suspend fun insert(entity: Entity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: Entity)

    @Insert
    suspend fun insertAll(list: List<Entity>)
    @Update
    suspend fun update(entity: Entity)

    @Update
    suspend fun updateAll(list: List<Entity>)

    @Delete
    suspend fun delete(entity: Entity)

    @Delete
    suspend fun deleteList(list: List<Entity>)

    suspend fun deleteAll()
}