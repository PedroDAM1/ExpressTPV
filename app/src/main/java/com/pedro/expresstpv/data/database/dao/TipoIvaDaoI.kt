package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.TipoIvaEntityI
import kotlinx.coroutines.flow.Flow

@Dao
interface TipoIvaDaoI : IBaseDao<TipoIvaEntityI> {
    @Insert
    suspend fun insert(tipoIva: TipoIvaEntityI)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listTipoIva: List<TipoIvaEntityI>)

    @Update
    suspend fun update(tipoIva: TipoIvaEntityI)

    @Delete
    suspend fun delete(tipoIva: TipoIvaEntityI)

    @Query("DELETE FROM tb_tipoiva")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_tipoiva WHERE id=:id")
    fun getById(id: Int): Flow<TipoIvaEntityI?>

    @Query("SELECT * FROM tb_tipoiva")
    override fun getAll(): Flow<List<TipoIvaEntityI>>
}