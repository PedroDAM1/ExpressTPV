package com.pedro.expresstpv.data.usecase

import android.util.Log
import com.pedro.expresstpv.data.provider.IBaseRepository
import com.pedro.expresstpv.domain.model.IBaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

abstract class BaseUseCase <Domain : IBaseModel> (private val repositoryBase: IBaseRepository<Domain>) {

    open fun getAllFlow() : Flow<List<Domain>> {
        //Vamos a filtrar para traernos unicamente los que tengan un id > 0 ya que el id 0 se suele usar para cargar datos basicos
        return repositoryBase.getAllFlow()
            .map {
                it.filter { domain ->
                    domain.id > 0
                }
            }
            .flowOn(Dispatchers.Default)
    }

    suspend fun getAll() : List<Domain> = withContext(Dispatchers.Default) {
        return@withContext repositoryBase.getAll()
            .filter {
                it.id > 0
            }
    }
    suspend fun getById(id : Int) = repositoryBase.getById(id)

    suspend fun insert(domain: Domain) {
        repositoryBase.insert(domain)
    }

    suspend fun insertAll(list: List<Domain>){
        repositoryBase.insertAll(list)
    }

    suspend fun update(domain: Domain){
        repositoryBase.update(domain)
    }

    suspend fun updateAll(list: List<Domain>){
        repositoryBase.updateAll(list)
    }

    suspend fun deleteList(list: List<Domain>){
        Log.d("DELETE LIST", "Eliminando una lista")
        repositoryBase.deleteList(list)
    }
    suspend fun deleteAll(){
        repositoryBase.deleteAll()
    }

    suspend fun delete(domain: Domain){
        repositoryBase.delete(domain)
    }
}