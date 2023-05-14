package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.LineaTicketDao
import com.pedro.expresstpv.data.database.entities.LineaTicketEntity
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.domain.model.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LineaTicketRepository @Inject constructor(
    private val lineaTicketDao: LineaTicketDao,
    private val ticketRepository: TicketRepository
) {

    private val _lineaTicketEntityFlow : Flow<List<LineaTicketEntity>> = lineaTicketDao.getAll()
    private val _lineaTicketFlow : Flow<List<LineaTicket>> = _lineaTicketEntityFlow
        .catch {
            Log.d("GET LINEATICKET", "ERROR AL OBTENER EL FLOW EN LA CAPA DE MODELO: ${it.message}")
        }
        .map {
            it.map { entity ->
                entity.toDomain()
            }
        }
        .flowOn(Dispatchers.IO)


    fun getAllLineaTicket() = _lineaTicketFlow

    suspend fun getLineaTicketById(id : Int) : LineaTicket? {
        return lineaTicketDao.getById(id)
            .catch {
                Log.d("GET LINEATICKET", "ERROR AL MAPEAR UNA LINEA TICKET: ${it.message}")
            }
            .map {
                it?.toDomain()
            }
            .flowOn(Dispatchers.IO)
            .first()
    }

    fun getLineasTicketsByNumTicket(numTicket : Int) = lineaTicketDao.getLineaTicketByNumTicket(numTicket)
        .map {
            it.map { lineaTicket ->
                lineaTicket.toDomain()
            }
        }
        .flowOn(Dispatchers.IO)

    suspend fun insertLineaTicket(lineaTicket: LineaTicket){
        lineaTicketDao.insert(lineaTicket.toEntity())
        Log.d("INSERT LINEATICKET", "Insertando LineaTicket: $lineaTicket")
    }

    suspend fun updateLineaTicket(lineaTicket: LineaTicket){
        lineaTicketDao.update(lineaTicket.toEntity())
        Log.d("UPDATE LIENATICKET", "Se ha actualizado una lineaTicket a: $lineaTicket")
    }

    suspend fun deleteAll(){
        lineaTicketDao.deleteAll()
    }
    private suspend fun LineaTicketEntity.toDomain() : LineaTicket{
        val ticket : Ticket = ticketRepository.getTicketByNumTicket(this.numTicket) ?: ticketRepository.getTicketByNumTicket(0)!!

        val lineaTicket = LineaTicket(
            id = this.id,
            ticket = ticket,
            descripcion = this.descripcion,
            categoriaVenta = this.categoriaVenta,
            cantidad = this.cantidad,
            valorIva = this.valorIva,
            subTotal = this.subtotal,
            total = this.total
        )

        Log.d("GET LINEATICKET", "Se esta mapeando la linea ticket: $lineaTicket")

        return lineaTicket
    }

    private fun LineaTicket.toEntity() : LineaTicketEntity{
        return LineaTicketEntity(
            id = this.id,
            numTicket = this.ticket.numTicket,
            descripcion = this.descripcion,
            categoriaVenta = this.categoriaVenta,
            cantidad = this.cantidad,
            valorIva = this.valorIva,
            subtotal = this.subTotal,
            total = this.total
        )
    }

}