package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.MetodoPagoEntity

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
    suspend fun getById(id: Int): MetodoPagoEntity?

    @Query("SELECT * FROM tb_metodopago")
    suspend fun getAll(): List<MetodoPagoEntity>
}