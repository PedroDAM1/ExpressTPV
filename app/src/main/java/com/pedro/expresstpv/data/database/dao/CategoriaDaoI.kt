package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.CategoriaEntityI
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDaoI : IBaseDao<CategoriaEntityI> {
    @Insert
    suspend fun insert(categoria: CategoriaEntityI)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listCategoria: List<CategoriaEntityI>)

    @Update
    suspend fun update(categoria: CategoriaEntityI)

    @Delete
    suspend fun delete(categoria: CategoriaEntityI)

    @Query("DELETE FROM tb_categoria")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_categoria WHERE id=:id")
    fun getById(id: Int): Flow<CategoriaEntityI?>

    @Query("SELECT * FROM tb_categoria")
    override fun getAll(): Flow<List<CategoriaEntityI>>

    @Query("SELECT id FROM tb_categoria ORDER BY id DESC LIMIT 1")
    suspend fun getLastId() : Int
}
