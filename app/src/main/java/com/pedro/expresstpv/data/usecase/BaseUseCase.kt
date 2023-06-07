package com.pedro.expresstpv.data.usecase

import android.util.Log
import com.pedro.expresstpv.data.provider.IBaseRepository
import com.pedro.expresstpv.domain.model.IBaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withContext

abstract class BaseUseCase <Domain : IBaseModel> (private val repositoryBase: IBaseRepository<Domain>) {

    private val semaphore = Semaphore(1)
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

    open suspend fun getAll() : List<Domain> = withContext(Dispatchers.Default) {
        return@withContext repositoryBase.getAll()
            .filter {
                it.id > 0
            }
    }
    suspend fun getById(id : Int) = repositoryBase.getById(id)

    suspend fun insert(domain: Domain) {
        try {
            semaphore.acquire()
            repositoryBase.insert(domain)
        } finally {
            semaphore.release()
        }
    }

    suspend fun insertAll(list: List<Domain>){
        try {
            semaphore.acquire()
            repositoryBase.insertAll(list)
        }finally {
            semaphore.release()
        }
    }

    suspend fun insertOrUpdate(domain: Domain){
        try{
            semaphore.acquire()
            repositoryBase.insertOrUpdate(domain)
        } finally {
            semaphore.release()
        }
    }

    suspend fun update(domain: Domain){
        try {
            semaphore.acquire()
            repositoryBase.update(domain)
        } finally {
            semaphore.release()
        }
    }

    suspend fun updateAll(list: List<Domain>){
        repositoryBase.updateAll(list)
    }

    suspend fun deleteList(list: List<Domain>){
        Log.d("DELETE LIST", "Eliminando una lista")
        repositoryBase.deleteList(list)
    }
    suspend fun deleteAll(){
        Log.d("DELETE ALL", "")
        repositoryBase.deleteAll()
    }

    suspend fun delete(domain: Domain){
        Log.d("DELETE", "Eliminando: $domain")
        repositoryBase.delete(domain)
    }
}