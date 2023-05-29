package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.IBaseRepository

abstract class BaseUseCase <Domain> (private val repositoryBase: IBaseRepository<Domain>) {

    fun getAllFlow() = repositoryBase.getAllFlow()

    suspend fun getAll() = repositoryBase.getAll()

    suspend fun getById(id : Int) = repositoryBase.getById(id)

}