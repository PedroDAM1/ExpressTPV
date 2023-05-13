package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.CierreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CierreDao {
    @Insert
    suspend fun insert(cierre: CierreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listCierre: List<CierreEntity>)

    @Update
    suspend fun update(cierre: CierreEntity)

    @Delete
    suspend fun delete(cierre: CierreEntity)

    @Query("DELETE FROM tb_cierre")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_cierre WHERE num_cierre=:numCierre")
    fun getByNumCierre(numCierre: Int): Flow<CierreEntity?>

    @Query("SELECT * FROM tb_cierre")
    fun getAll(): Flow<List<CierreEntity>>
}