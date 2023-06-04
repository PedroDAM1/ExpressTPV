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
) : BaseUseCase<Cierre>(cierresRepository) {

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

    suspend fun getLastNumCierre() : Int = cierresRepository.getLastNumCierre()

    suspend fun crearCierre() : Cierre = withContext(Dispatchers.IO){
        val newNumCierre = getLastNumCierre()+1
        val cierre = Cierre(
            id = 0,
            numCierre = newNumCierre
        )

        this@CierreUseCase.insert(cierre)

        return@withContext this@CierreUseCase.getCierreByNumCierre(newNumCierre)!!
    }

}