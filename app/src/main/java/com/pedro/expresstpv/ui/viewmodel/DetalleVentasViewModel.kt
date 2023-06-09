package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.LineaTicketUseCases
import com.pedro.expresstpv.data.usecase.TicketUseCase
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.domain.model.Ticket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class DetalleVentasViewModel @Inject constructor (
    private val lineaTicketUseCases: LineaTicketUseCases,
    private val ticketUseCases: TicketUseCase
    ) : ViewModel() {

    fun getFechaInicio() = fechaInicio
    fun getFechaFin() = fechaFin

    private var fechaInicio: LocalDate = LocalDate.now().minusDays(7)
    private var fechaFin : LocalDate = LocalDate.now()

    private var _listaTickets : List<Ticket> = listOf()
    private var _listaLineaTickets : List<LineaTicket> = listOf()

    private var _mutableStateFlow : MutableStateFlow<UIStateDetalles> = MutableStateFlow(UIStateDetalles.Loading)
    val uiState : StateFlow<UIStateDetalles> = _mutableStateFlow

    init {
        updateFechas(fechaInicio, fechaFin)

    }

    private suspend fun getListaTicketsBetweenFechas(fechaInicio : LocalDate, fechaFin : LocalDate) =
        ticketUseCases.getTicketBetweenFechas(fechaInicio.atStartOfDay(), fechaFin.atTime(LocalTime.MAX))


    fun updateFechas(fechaInicio : LocalDate, fechaFin : LocalDate){
        this.fechaInicio = fechaInicio
        this.fechaFin = fechaFin

        //Actualizamos la nueva lista al recycler
        viewModelScope.launch (Dispatchers.Default) {
            _listaTickets = getListaTicketsBetweenFechas(this@DetalleVentasViewModel.fechaInicio, this@DetalleVentasViewModel.fechaFin)
            _listaLineaTickets = lineaTicketUseCases.getLineaTicketAcumuladosFromTickets(_listaTickets)
            _mutableStateFlow.value = UIStateDetalles.Succes(_listaLineaTickets)
        }
    }

    fun getTotalFromLineaTickets() = lineaTicketUseCases.getTotalFromLineaTickets(_listaLineaTickets)

    fun getSubtotalFromLineaTickets() = lineaTicketUseCases.getSubtotalFromLineaTickets(_listaLineaTickets)

    sealed class UIStateDetalles{
        object Loading : UIStateDetalles()
        data class Succes(val lista : List<LineaTicket>) : UIStateDetalles()
        data class Error(val msg : String) : UIStateDetalles()
    }
}