package com.pedro.expresstpv.data.database.dao

import androidx.room.Dao
import androidx.room.*
import com.pedro.expresstpv.data.database.entities.ArticuloConCategoriaETipoIva
import com.pedro.expresstpv.data.database.entities.ArticuloEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticuloDao {
    @Insert
    suspend fun insert(articulo: ArticuloEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listArticulo: List<ArticuloEntity>)

    @Update
    suspend fun update(articulo: ArticuloEntity)

    @Delete
    suspend fun delete(articulo: ArticuloEntity)

    @Query("DELETE FROM tb_articulo")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_articulo WHERE id=:id")
    suspend fun getById(id: Int): ArticuloEntity?

    @Query("SELECT * FROM tb_articulo")
    fun getAll(): Flow<List<ArticuloEntity>>

    @Transaction
    @Query("SELECT * FROM tb_articulo")
    suspend fun getArticuloConCategoriaYTipoIva():List<ArticuloConCategoriaETipoIva>

    @Transaction
    @Query("SELECT * FROM tb_articulo WHERE id=:id")
    suspend fun getArticuloConCategoriaYTipoIvaById(id: Int) : ArticuloConCategoriaETipoIva?
}