package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.provider.ArticuloRepository
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.Articulo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ListaArticulosViewModel @Inject constructor(
   articuloRepository: ArticuloRepository
) : ViewModel() {

    //val _listaArticulos : Flow<List<Articulo>> = articuloRepository.getAllArticulos()
    //val listaArticulos : StateFlow<UIState> = Functions.getStateFlow(viewModelScope, _listaArticulos)


}