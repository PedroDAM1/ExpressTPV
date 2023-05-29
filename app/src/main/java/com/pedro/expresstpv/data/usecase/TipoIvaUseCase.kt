package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.TipoIvaRepository
import com.pedro.expresstpv.domain.model.TipoIva
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TipoIvaUseCase @Inject constructor(
    private val tipoIvaRepository: TipoIvaRepository
) : BaseUseCase<TipoIva>(tipoIvaRepository) {


}