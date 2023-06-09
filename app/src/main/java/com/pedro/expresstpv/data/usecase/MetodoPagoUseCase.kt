package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.MetodoPagoRepository
import com.pedro.expresstpv.domain.model.MetodoPago
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetodoPagoUseCase @Inject constructor(
    metodoPagoRepository: MetodoPagoRepository
) : BaseUseCase<MetodoPago>(metodoPagoRepository) {

    suspend fun getMetodoPagoByDefault() : MetodoPago = withContext(Dispatchers.IO){
        var metodo = this@MetodoPagoUseCase.getById(0)
        if (metodo == null){
            metodo = MetodoPago(id = 0, nombre = "SIN DEFINIR")
        }

        return@withContext metodo
    }

}