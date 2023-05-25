package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.LineaTicketRepository
import com.pedro.expresstpv.data.provider.TicketRepository
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.domain.model.Ticket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketUseCase @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val lineaTicketUseCases: LineaTicketUseCases
) {

    suspend fun createTicketFromLineaTicketsActivo(){

    }

}