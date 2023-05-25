package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.TicketDao
import com.pedro.expresstpv.data.database.entities.TicketEntity
import com.pedro.expresstpv.domain.model.Cierre
import com.pedro.expresstpv.domain.model.MetodoPago
import com.pedro.expresstpv.domain.model.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao,
    private val cierreRepository : CierresRepository,
    private val metodoPagoRepository: MetodoPagoRepository
) {

    private val _ticketEntityFlow : Flow<List<TicketEntity>> = ticketDao.getAll()
    private val _listaTicketFlow : Flow<List<Ticket>> = _ticketEntityFlow
        .map {
            it.map { entity ->
                entity.toDomain()
            }
        }
        .flowOn(Dispatchers.IO)

    fun getAllTicket() = _listaTicketFlow

    suspend fun insertTicket(ticket: Ticket){
        ticketDao.insert(ticket.toEntity())
    }

    suspend fun getTicketByNumTicket(numTicket : Int) : Ticket? {
        return ticketDao.getByNumTicket(numTicket)
            .map {
                it?.toDomain()
            }
            .flowOn(Dispatchers.IO)
            .lastOrNull()
    }

    private fun Ticket.toEntity() : TicketEntity{
        return TicketEntity(
            numTicket = this.numTicket,
            numCierre = this.cierre.numCierre,
            idMetodopago = this.metodoPago.id,
            fecha = this.fecha,
            total = this.total
        )
    }
    private suspend fun TicketEntity.toDomain() : Ticket {
        //Los objetos con id 0 de la base de datos son objetos base para cuando aun no se le asigno nada al objeto
        val cierre : Cierre = cierreRepository.getCierreByNumCierre(this.numCierre) ?: cierreRepository.getCierreByNumCierre(0)!!
        val metodoPago : MetodoPago = metodoPagoRepository.getMetodoPagoById(this.idMetodopago) ?: metodoPagoRepository.getMetodoPagoById(0)!!


        val ticket = Ticket(
            numTicket = this.numTicket,
            cierre = cierre,
            metodoPago = metodoPago,
            fecha = this.fecha,
            total = this.total

        )
        Log.d("GET TICKET", "Se ha mapeado un ticket: $ticket")
        return ticket
    }

}