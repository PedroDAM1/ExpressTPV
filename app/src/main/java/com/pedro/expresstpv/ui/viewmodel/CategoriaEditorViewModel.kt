package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.CategoriaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriaEditorViewModel @Inject constructor(
    private val categoriaUseCase: CategoriaUseCase
    ) : ViewModel() {

    fun guardarDatos(nombre : String, color: String){

        viewModelScope.launch {
            categoriaUseCase.insertCategoria(nombre, color)
        }

    }



}