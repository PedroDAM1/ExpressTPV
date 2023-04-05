package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.CierreEntity

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
    suspend fun getByNumCierre(numCierre: Int): CierreEntity?

    @Query("SELECT * FROM tb_cierre")
    suspend fun getAll(): List<CierreEntity>
}