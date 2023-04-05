package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.database.AccesoDatos
import com.pedro.expresstpv.data.database.dao.ArticuloDao
import com.pedro.expresstpv.data.database.entities.ArticuloConCategoriaETipoIva
import com.pedro.expresstpv.data.database.entities.ArticuloEntity
import com.pedro.expresstpv.di.RoomModule
import com.pedro.expresstpv.domain.model.Articulo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentasViewModel @Inject constructor(private val articuloDao : ArticuloDao) : ViewModel(){

    var liveDataArticulos = MutableLiveData<List<ArticuloConCategoriaETipoIva>>()

    fun onCreate(){
        viewModelScope.launch {
            liveDataArticulos.value = articuloDao.getArticuloConCategoriaYTipoIva()
        }
    }

}