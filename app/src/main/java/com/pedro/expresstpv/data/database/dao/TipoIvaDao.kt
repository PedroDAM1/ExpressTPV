package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.TipoIvaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TipoIvaDao {
    @Insert
    suspend fun insert(tipoIva: TipoIvaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listTipoIva: List<TipoIvaEntity>)

    @Update
    suspend fun update(tipoIva: TipoIvaEntity)

    @Delete
    suspend fun delete(tipoIva: TipoIvaEntity)

    @Query("DELETE FROM tb_tipoiva")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_tipoiva WHERE id=:id")
    fun getById(id: Int): Flow<TipoIvaEntity?>

    @Query("SELECT * FROM tb_tipoiva")
    fun getAll(): Flow<List<TipoIvaEntity>>
}