package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.CierreUseCase
import com.pedro.expresstpv.data.usecase.LineaTicketUseCases
import com.pedro.expresstpv.data.usecase.MetodoPagoUseCase
import com.pedro.expresstpv.data.usecase.TicketUseCase
import com.pedro.expresstpv.domain.model.Ticket
import com.pedro.expresstpv.ui.adapters.GrillaMetodosPagoCierresListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CierresViewModel @Inject constructor(
    private val ticketUseCases: TicketUseCase,
    private val metodoPagoUseCase: MetodoPagoUseCase,
    private val cierreUseCase: CierreUseCase
) : ViewModel() {


    private val _flowTotalMetodoPago : Flow<List<GrillaMetodosPagoCierresListAdapter.MetodosPagoYTotalesTicket>> = metodoPagoUseCase.getAllFlow()
        .map {
            it.map { metodoPago ->
                GrillaMetodosPagoCierresListAdapter.MetodosPagoYTotalesTicket(metodoPago, ticketUseCases.getTotalTicketPorMetodoPagoParaCierreActivo(metodoPago))
            }
        }

    suspend fun getListaTickets() = ticketUseCases.getAllTicketFromCierreActivoFlow()

    suspend fun getTotalTickets() = ticketUseCases.getSumOfTicketsFromCierreActivo()

    fun getTotalPorMetodoPagoFlow() = _flowTotalMetodoPago

    fun crearCierre(){
        viewModelScope.launch(Dispatchers.IO) {
            val cierre = cierreUseCase.crearCierre()
            ticketUseCases.updateTicketsFromCierreActivoToNewCierre(cierre)
        }
    }


}