package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.provider.CategoriaRepository
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.Categoria
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ListaCategoriasViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {


    //Flow
    private val _listaCategoriasUIState = Functions.getStateFlow(viewModelScope, categoriaRepository.categoriaFlow)
    val listaCategoriasUIState : StateFlow<UIState> = _listaCategoriasUIState

    init {
        onCreate()
    }

    private fun onCreate(){
        //Empezamos a escuchar el flow que conecta con la base de datos
//        categoriaRepository.categoriaFlow
//            .onEach {
//                _listaCategoriasUIState.value = UIState.Succes(categoriaRepository.categoriaFlow)
//            }
//            .catch {
//                _listaCategoriasUIState.value = UIState.Error(it.message.orEmpty())
//            }
//            .flowOn(Dispatchers.IO)
//            .launchIn(viewModelScope)
    }


}