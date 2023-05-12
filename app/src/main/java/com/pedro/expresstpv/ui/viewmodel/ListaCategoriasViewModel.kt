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
    val listaCategoriasUIState : StateFlow<UIState> = Functions.getStateFlow(viewModelScope, categoriaRepository.getAllCategorias())

    init {
        onCreate()
    }

    private fun onCreate(){
        //Empezamos a escuchar el flow que conecta con la base de datos
//        categoriaRepository.getAllCategorias()
//            .onEach {
//                _listaCategoriasUIState.value = UIState.Succes(categoriaRepository.getAllCategorias())
//            }
//            .catch {
//                _listaCategoriasUIState.value = UIState.Error(it.message.orEmpty())
//            }
//            .flowOn(Dispatchers.IO)
//            .launchIn(viewModelScope)
    }


}