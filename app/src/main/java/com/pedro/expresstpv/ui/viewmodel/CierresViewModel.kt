package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.LineaTicketUseCases
import com.pedro.expresstpv.data.usecase.TicketUseCase
import com.pedro.expresstpv.domain.model.Ticket
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CierresViewModel @Inject constructor(
    private val ticketUseCases: TicketUseCase
) : ViewModel() {

    private lateinit var _listaTicketsActivo : List<Ticket>

    init {
        onCreate()
    }

    private fun onCreate(){
        viewModelScope.launch {
            _listaTicketsActivo = ticketUseCases.getAllTicketFromCierreActivo()
        }
    }

    fun getListaTickets() = _listaTicketsActivo

    suspend fun getTotalTickets() = ticketUseCases.getSumOfTicketsFromCierreActivo()

}