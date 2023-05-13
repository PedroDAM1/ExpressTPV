package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.MetodoPagoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MetodoPagoDao {
    @Insert
    suspend fun insert(metodoPago: MetodoPagoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listMetodoPago: List<MetodoPagoEntity>)

    @Update
    suspend fun update(metodoPago: MetodoPagoEntity)

    @Delete
    suspend fun delete(metodoPago: MetodoPagoEntity)

    @Query("DELETE FROM tb_metodopago")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_metodopago WHERE id=:id")
    fun getById(id: Int): Flow<MetodoPagoEntity?>

    @Query("SELECT * FROM tb_metodopago")
    fun getAll(): Flow<List<MetodoPagoEntity>>
}