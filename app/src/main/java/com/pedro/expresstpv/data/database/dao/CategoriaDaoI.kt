package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDaoI : IBaseDao<CategoriaEntity> {
//    @Insert
//    override suspend fun insert(entity: CategoriaEntity)
//
//    @Insert
//    override suspend fun insertAll(list: List<CategoriaEntity>)
//
//    @Update
//    override suspend fun update(entity: CategoriaEntity)
//
//
//    @Delete
//    override suspend fun delete(entity: CategoriaEntity)

    @Query("DELETE FROM tb_categoria")
    override suspend fun deleteAll()

    @Query("SELECT * FROM tb_categoria WHERE id=:id")
    fun getById(id: Int): Flow<CategoriaEntity?>

    @Query("SELECT * FROM tb_categoria")
    override fun getAll(): Flow<List<CategoriaEntity>>

    @Query("SELECT id FROM tb_categoria ORDER BY id DESC LIMIT 1")
    suspend fun getLastId() : Int
}
