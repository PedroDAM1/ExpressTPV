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

/**
 * Clase base desde la cual podremos hacer las operaciones basicas de un use case
 */
abstract class BaseUseCase <Domain : IBaseModel> (private val repositoryBase: IBaseRepository<Domain>) {

    //Control para evitar saturacion en las insercciones
    private val semaphore = Semaphore(1)

    /**
     *  Devuelve el flow de la lista de domians que superen el id 0 para evitar los objetos creados por defecto
     *  @return Flow<List<Domain>>
     */
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

    /**
     * Devolvemos la ultima actualizacion de la base de datos con los objetos pedidos
     * @return List<Domain>
     */
    open suspend fun getAll() : List<Domain> = withContext(Dispatchers.Default) {
        return@withContext repositoryBase.getAll()
            .filter {
                it.id > 0
            }
    }

    /**
     * Devuelve un objeto Domain buscandolo por su id, puede devolver null si no existe
     *
     * @param id id por el que queremos buscar
     * @return Domain obtenido, Null si no existe un Domain con ese Id
     */
    suspend fun getById(id : Int) = repositoryBase.getById(id)

    /**
     * Permite insertar un objeto en base de datos
     * La gestion la hace a traves de un semaforo para evitar saturacion
     * @param domain Objeto a insertar
     */
    suspend fun insert(domain: Domain) {
        try {
            semaphore.acquire()
            repositoryBase.insert(domain)
        } finally {
            semaphore.release()
        }
    }

    /**
     * Permite insertar una lista de objetos
     * @param list Lista de objetos para insertar
     */
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

    /**
     * Permite actualizar el elemento pasado por parametro
     * @param domain Elemento a actualizar
     */
    suspend fun update(domain: Domain){
        try {
            semaphore.acquire()
            repositoryBase.update(domain)
        } finally {
            semaphore.release()
        }
    }

    /**
     * Actualiza la lista de elementos pasados por parametro
     * @param list
     */
    suspend fun updateAll(list: List<Domain>){
        repositoryBase.updateAll(list)
    }

    /**
     * Elimina de la base de datos la lista de articulos pasada
     * @param list
     */
    suspend fun deleteList(list: List<Domain>){
        repositoryBase.deleteList(list)
    }

    /**
     * Trunca la tabla de la base de datos que pertenezca a este use case
     */
    suspend fun deleteAll(){
        repositoryBase.deleteAll()
    }

    /**
     * Elimina el elemento pasado de la base de datos
     * @param domain
     */
    suspend fun delete(domain: Domain){
        repositoryBase.delete(domain)
    }
}