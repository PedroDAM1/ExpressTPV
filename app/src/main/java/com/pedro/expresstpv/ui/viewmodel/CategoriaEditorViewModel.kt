package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.CategoriaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriaEditorViewModel @Inject constructor(
    private val categoriaUseCase: CategoriaUseCase
    ) : ViewModel() {

    /**
     * Se encargara de llamar al useCase para crear o actualizar la categoria
     */
    fun guardarDatos(id : Int, nombre : String, color: String){
        viewModelScope.launch (Dispatchers.IO) {
            // Si nos llega un 0 es por que estamos insertando una nueva categoria
            if (id != 0){
                categoriaUseCase.updateCategoria(id, nombre, color)
            } else {
                categoriaUseCase.insertCategoria(nombre, color)
            }
        }
    }



}