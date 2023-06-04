package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.LineaTicketUseCases
import com.pedro.expresstpv.data.usecase.MetodoPagoUseCase
import com.pedro.expresstpv.data.usecase.TicketUseCase
import com.pedro.expresstpv.domain.model.MetodoPago
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CobrosViewModel @Inject constructor(
    private val lineaTicketUseCases: LineaTicketUseCases,
    private val metodoPagoUseCase: MetodoPagoUseCase,
    private val ticketUseCases: TicketUseCase
) : ViewModel() {

    suspend fun getMetodosPago() = metodoPagoUseCase.getAll()
    suspend fun getTotalTicket() : Double = withContext(Dispatchers.IO){
        return@withContext lineaTicketUseCases.getTotalFromLineaTicketsActivo()
    }

    fun crearTicket(metodoPago: MetodoPago){
        viewModelScope.launch (Dispatchers.IO){
            val ticket = ticketUseCases.crearTicket(metodoPago, getTotalTicket(), 0.00)
            lineaTicketUseCases.updateLineaTicketsActivoToNewTicket(ticket)
            //Una vez que creemos el ticket deberemos de actualizar las lineasTickets actuales para apuntar a ese ticket
        }
    }

}