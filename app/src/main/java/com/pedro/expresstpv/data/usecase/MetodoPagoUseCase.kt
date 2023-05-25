package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.MetodoPagoRepository
import com.pedro.expresstpv.domain.model.MetodoPago
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetodoPagoUseCase @Inject constructor(
    private val metodoPagoRepository: MetodoPagoRepository
) {

    suspend fun getMetodosPago() : List<MetodoPago>{
        return metodoPagoRepository.getAllMetodoPago()
            .map { it.filterNot { it.id == 0 } }
            .flowOn(Dispatchers.IO)
            .first()
    }

    fun getMetodosPagoFlow() = metodoPagoRepository.getAllMetodoPago()

}