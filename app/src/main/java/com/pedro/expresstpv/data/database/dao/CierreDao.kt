package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.CierreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CierreDao : IBaseDao<CierreEntity> {
    @Query("DELETE FROM tb_cierre")
    override suspend fun deleteAll()

    @Query("SELECT * FROM tb_cierre WHERE num_cierre=:numCierre")
    fun getByNumCierre(numCierre: Int): Flow<CierreEntity?>

    @Query("SELECT * FROM tb_cierre")
    override fun getAll(): Flow<List<CierreEntity>>

    @Query("SELECT num_cierre FROM tb_cierre ORDER BY num_cierre DESC LIMIT 1")
    suspend fun getLastNumCierre() : Int
}