package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.pedro.expresstpv.data.provider.ArticuloRepository
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.LineaTicket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class VentasViewModel @Inject constructor(private val articuloRepository: ArticuloRepository) : ViewModel() {

    private val _listaArticulos = articuloRepository.getAllArticulos()
    private val lineaTicketFlow : Flow<List<LineaTicket>> = flowOf()
    private val listaLineaTickets : MutableList<LineaTicket> = mutableListOf()

    fun getAllArticulos() = _listaArticulos

    fun getListaArticulosPorCategoria(idCategoria : Int){
        //TODO Hacer que la funcion devuelva los articulos que pertenezcan a una categoria concreta
    }

    fun insertarEnLineaTicket(){

    }

    private fun modificarLineaTicket(){

    }

    private fun crearLineaTicket(){

    }

    private fun comprobarLineaTicket(articulo : Articulo) : Boolean{
        listaLineaTickets.forEach {
            //Vamos a comprobar si las caracteristicas del articulo coinciden con las de la linea ticket
            return it.descripcion == articulo.nombre &&
                    (it.total/it.cantidad) == articulo.precio &&
                    it.valorIva == articulo.tipoIva.porcentaje
        }
        return false
    }
}