package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.LineaTicketEntity

@Dao
interface LineaTicketDao {
    @Insert
    suspend fun insert(lineaTicket: LineaTicketEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listLineaTicket: List<LineaTicketEntity>)

    @Update
    suspend fun update(lineaTicket: LineaTicketEntity)

    @Delete
    suspend fun delete(lineaTicket: LineaTicketEntity)

    @Query("DELETE FROM tb_lineaticket")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_lineaticket WHERE id=:id")
    suspend fun getById(id: Int): LineaTicketEntity?

    @Query("SELECT * FROM tb_lineaticket")
    suspend fun getAll(): List<LineaTicketEntity>
}