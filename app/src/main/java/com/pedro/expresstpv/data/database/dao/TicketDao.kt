package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.TicketEntity

@Dao
interface TicketDao {
    @Insert
    suspend fun insert(ticket: TicketEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listTicket: List<TicketEntity>)

    @Update
    suspend fun update(ticket: TicketEntity)

    @Delete
    suspend fun delete(ticket: TicketEntity)

    @Query("DELETE FROM tb_ticket")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_ticket WHERE num_ticket=:numTicket")
    suspend fun getByNumTicket(numTicket: Int): TicketEntity?

    @Query("SELECT * FROM tb_ticket")
    suspend fun getAll(): List<TicketEntity>
}