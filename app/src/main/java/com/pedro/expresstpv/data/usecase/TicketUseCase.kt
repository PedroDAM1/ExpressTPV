package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.TicketRepository
import com.pedro.expresstpv.domain.model.MetodoPago
import com.pedro.expresstpv.domain.model.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketUseCase @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val lineaTicketUseCases: LineaTicketUseCases,
    private val cierreUseCase: CierreUseCase
) {

    suspend fun crearTicket(metodoPago: MetodoPago, total : Double, subtotal : Double) = withContext(Dispatchers.IO){
        val cierreActivo = cierreUseCase.getCierreActivo()
        val lastTicket = getLastNumTicket()

        val ticket = Ticket(
            //Insertamos el ticket con un nuevo id
            numTicket = lastTicket+1,
            cierre = cierreActivo,
            metodoPago = metodoPago,
            total = total)

        ticketRepository.insertTicket(ticket)

        lineaTicketUseCases.updateLineaTicketsActivoToNewTicket(ticket)
    }

    suspend fun getLastNumTicket() : Int = ticketRepository.getLastNumTicket()

    suspend fun getTicketByNumTicket(num : Int) : Ticket? {
        return ticketRepository.getTicketByNumTicket(num)
    }

    suspend fun getTicketActivo() : Ticket {
        return ticketRepository.getTicketByNumTicket(0)!!

    }

}