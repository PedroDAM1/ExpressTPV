package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.CategoriaEntity

@Dao
interface CategoriaDao {
    @Insert
    suspend fun insert(categoria: CategoriaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listCategoria: List<CategoriaEntity>)

    @Update
    suspend fun update(categoria: CategoriaEntity)

    @Delete
    suspend fun delete(categoria: CategoriaEntity)

    @Query("DELETE FROM tb_categoria")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_categoria WHERE id=:id")
    suspend fun getById(id: Int): CategoriaEntity?

    @Query("SELECT * FROM tb_categoria")
    suspend fun getAll(): List<CategoriaEntity>
}
