package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.CierresRepository
import com.pedro.expresstpv.domain.model.Cierre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CierreUseCase @Inject constructor(
    private val cierresRepository: CierresRepository
) {

    suspend fun getCierreByNumCierre(num : Int) : Cierre? {
        return cierresRepository.getAll()
            .firstOrNull {
                it.numCierre == num
            }
    }
    suspend fun getCierreActivo() : Cierre = withContext(Dispatchers.IO){
        val cierre = cierresRepository.getById(0)

        return@withContext cierre ?: Cierre(0, 0, null)
    }

}