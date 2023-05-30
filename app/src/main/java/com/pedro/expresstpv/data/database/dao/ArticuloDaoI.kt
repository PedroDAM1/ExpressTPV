package com.pedro.expresstpv.data.database.dao

import androidx.room.Dao
import androidx.room.*
import com.pedro.expresstpv.data.database.entities.ArticuloEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticuloDaoI : IBaseDao<ArticuloEntity> {
    @Insert
    override suspend fun insert(entity: ArticuloEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listArticulo: List<ArticuloEntity>)

    @Update
    override suspend fun update(entity: ArticuloEntity)

    @Delete
    override suspend fun delete(entity: ArticuloEntity)

    @Query("DELETE FROM tb_articulo")
    override suspend fun deleteAll()

    @Query("SELECT * FROM tb_articulo WHERE id=:id")
    suspend fun getById(id: Int): ArticuloEntity?

    @Query("SELECT * FROM tb_articulo")
    override fun getAll(): Flow<List<ArticuloEntity>>

}