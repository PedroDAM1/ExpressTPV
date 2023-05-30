package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.CierreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CierreDao : IBaseDao<CierreEntity> {
//    @Insert
//    override suspend fun insert(entity: CierreEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOrUpdateAll(listCierre: List<CierreEntity>)
//
//    @Update
//    suspend fun update(cierre: CierreEntity)
//
//    @Delete
//    suspend fun delete(cierre: CierreEntity)

    @Query("DELETE FROM tb_cierre")
    override suspend fun deleteAll()

    @Query("SELECT * FROM tb_cierre WHERE num_cierre=:numCierre")
    fun getByNumCierre(numCierre: Int): Flow<CierreEntity?>

    @Query("SELECT * FROM tb_cierre")
    override fun getAll(): Flow<List<CierreEntity>>
}