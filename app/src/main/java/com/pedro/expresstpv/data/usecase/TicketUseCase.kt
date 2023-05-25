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

        val ticket = Ticket(
            numTicket = 0,
            cierre =cierreActivo,
            metodoPago = metodoPago,
            total = total)

        ticketRepository.insertTicket(ticket)
    }

}