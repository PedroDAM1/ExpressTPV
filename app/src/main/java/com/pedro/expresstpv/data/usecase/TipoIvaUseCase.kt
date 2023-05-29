package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.TipoIvaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TipoIvaUseCase @Inject constructor(
    private val tipoIvaRepository: TipoIvaRepository
) {

    fun getAllTipoIvaFlow() = tipoIvaRepository.getAllTipoIva()

    suspend fun getTipoIvaById(id : Int) = tipoIvaRepository.getTipoIvaById(id)

}