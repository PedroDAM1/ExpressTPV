package com.pedro.expresstpv.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.provider.CategoriaRepository
import com.pedro.expresstpv.data.usecase.CategoriaUseCase
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.Categoria
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaCategoriasViewModel @Inject constructor(
    private val categoriaUseCase: CategoriaUseCase
) : ViewModel() {


    //Flow
//    val listaCategoriasUIState : StateFlow<UIState> = Functions.getStateFlow(viewModelScope, categoriaUseCase.getAllCategoriasFlow())

    suspend fun getListaCategoriasUIState() : StateFlow<UIState> {
        return Functions.getStateFlow(viewModelScope, categoriaUseCase.getAllFlow())
    }


}