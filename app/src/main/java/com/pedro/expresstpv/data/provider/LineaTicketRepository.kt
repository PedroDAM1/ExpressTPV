package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.LineaTicketDao
import com.pedro.expresstpv.data.database.entities.LineaTicketEntity
import com.pedro.expresstpv.data.usecase.TicketUseCase
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.domain.model.Ticket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LineaTicketRepository @Inject constructor(
    private val lineaTicketDao: LineaTicketDao,
    private val ticketUseCase: TicketUseCase
) : BaseRepository<LineaTicket, LineaTicketEntity>(lineaTicketDao) {


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

    override suspend fun toEntity(domain: LineaTicket): LineaTicketEntity {
        return domain.toEntity()
    }

    override suspend fun toDomain(entity: LineaTicketEntity): LineaTicket {
        val ticket : Ticket = ticketUseCase.getByNumTicket(entity.numTicket) ?: ticketUseCase.getTicketActivo()

        val lineaTicket = LineaTicket(
            id = entity.id,
            ticket = ticket,
            descripcion = entity.descripcion,
            categoriaVenta = entity.categoriaVenta,
            cantidad = entity.cantidad,
            valorIva = entity.valorIva,
            subTotal = entity.subtotal,
            total = entity.total
        )

        Log.d("GET LINEATICKET", "Se esta mapeando la linea ticket: $lineaTicket")

        return lineaTicket
    }

}