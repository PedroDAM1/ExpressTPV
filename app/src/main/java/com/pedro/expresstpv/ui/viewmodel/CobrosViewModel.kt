package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.LineaTicketUseCases
import com.pedro.expresstpv.data.usecase.MetodoPagoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CobrosViewModel @Inject constructor(
    private val lineaTicketUseCases: LineaTicketUseCases,
    private val metodoPagoUseCase: MetodoPagoUseCase
) : ViewModel() {

    suspend fun getMetodosPago() = metodoPagoUseCase.getMetodosPago()

    suspend fun getTotalTicket() : Double = withContext(Dispatchers.IO){
        return@withContext lineaTicketUseCases.getTotalFromLineaTicketsActivo()
    }

}