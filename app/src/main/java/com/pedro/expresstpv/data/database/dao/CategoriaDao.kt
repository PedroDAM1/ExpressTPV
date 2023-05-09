package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.CategoriaEntity
import kotlinx.coroutines.flow.Flow

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
    fun getAll(): Flow<List<CategoriaEntity>>

    @Query("SELECT id FROM tb_categoria ORDER BY id DESC LIMIT 1")
    suspend fun getLastId() : Int
}
