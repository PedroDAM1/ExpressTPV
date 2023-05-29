package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.provider.ArticuloRepository
import com.pedro.expresstpv.data.usecase.ArticulosUseCase
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.Articulo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ListaArticulosViewModel @Inject constructor(
    articulosUseCase: ArticulosUseCase
) : ViewModel() {

    //En la uiState mostraremos el loading mientras carga la lista de articulos
    val listaArticulos : StateFlow<UIState> = Functions.getStateFlow(viewModelScope, articulosUseCase.getAllArticulosFlow())


}