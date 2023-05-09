package com.pedro.expresstpv.ui.viewmodel

import kotlinx.coroutines.flow.Flow

sealed class UIState{
    object Loading : UIState()
    data class Succes<T>(val flow: Flow<T>) : UIState()
    data class Error(val msg : String) : UIState()
}