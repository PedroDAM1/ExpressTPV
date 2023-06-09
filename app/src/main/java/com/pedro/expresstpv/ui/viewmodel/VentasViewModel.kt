package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.ArticulosUseCase
import com.pedro.expresstpv.data.usecase.LineaTicketUseCases
import com.pedro.expresstpv.data.usecase.TicketUseCase
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.ui.adapters.VentasCalculadoraListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentasViewModel @Inject constructor(
    articulosUseCase: ArticulosUseCase,
    private val lineaTicketUseCases: LineaTicketUseCases,
    private val ticketUseCase: TicketUseCase
) : ViewModel() {

    private val _listaArticulos = articulosUseCase.getAllArticulosFlow()
    private val _lineaTicketActivoFlow = lineaTicketUseCases.getLineaTicketActivoFlow()

    private var _articuloConCantidadFlow : Flow<List<VentasCalculadoraListAdapter.ArticuloYCantidad>> = _listaArticulos.combine(_lineaTicketActivoFlow) { listaArticulos, _ ->
        listaArticulos.map { articulo ->
            val lineaTicket = lineaTicketUseCases.getLineaTicketWhenFeaturesActivo(articulo.nombre, articulo.categoria.nombre, articulo.tipoIva.porcentaje)
            VentasCalculadoraListAdapter.ArticuloYCantidad(articulo, lineaTicket?.cantidad ?: 0)
        }
    }
        .flowOn(Dispatchers.Default)


    fun deleteAll(){
        viewModelScope.launch {
            lineaTicketUseCases.deleteAll()
            ticketUseCase.deleteAll()
        }
    }

    fun getArticulosConCantidad() = _articuloConCantidadFlow

    fun getLineaTicketActivo() = _lineaTicketActivoFlow

    suspend fun getTotalesTicket() = lineaTicketUseCases.getTotalFromLineaTicketsActivo()
    fun eliminarTicketActual(){
        viewModelScope.launch (Dispatchers.IO) {
            lineaTicketUseCases.eliminarTicketActivo()
        }
    }

    fun getListaArticulosPorCategoria(idCategoria : Int){
        //TODO Hacer que la funcion devuelva los articulos que pertenezcan a una categoria concreta
    }

    /**
     * Obtenemos el articulo que se ha clicado en el boton del adapter, ademas deberemos de devolver la cantidad de lineaTicket de ese articulo
     */
    fun onArticuloItemClick(articulo: VentasCalculadoraListAdapter.ArticuloYCantidad) {
        viewModelScope.launch (Dispatchers.Default) {
            lineaTicketUseCases.crearLineaTicketActivo(articulo.articulo)
        }
    }

    fun reducirCantidadLineaTicket(lineaTicket: LineaTicket){
        viewModelScope.launch(Dispatchers.IO) {
            lineaTicketUseCases.reducirCantidadLineaTicket(lineaTicket, 1)
        }
    }

    fun aumentarCantidadLineaTicket(lineaTicket: LineaTicket){
        viewModelScope.launch (Dispatchers.IO) {
            lineaTicketUseCases.aumentarCantidadLineaTicket(lineaTicket, 1)
        }
    }

}