package com.pedro.expresstpv.data.database.dao

import androidx.room.Dao
import androidx.room.*
import com.pedro.expresstpv.data.database.entities.ArticuloEntityI
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticuloDaoI : IBaseDao<ArticuloEntityI> {
    @Insert
    suspend fun insert(articulo: ArticuloEntityI)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listArticulo: List<ArticuloEntityI>)

    @Update
    suspend fun update(articulo: ArticuloEntityI)

    @Delete
    suspend fun delete(articulo: ArticuloEntityI)

    @Query("DELETE FROM tb_articulo")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_articulo WHERE id=:id")
    suspend fun getById(id: Int): ArticuloEntityI?

    @Query("SELECT * FROM tb_articulo")
    override fun getAll(): Flow<List<ArticuloEntityI>>

}