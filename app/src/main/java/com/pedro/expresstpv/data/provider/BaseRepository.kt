package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.IBaseDao
import com.pedro.expresstpv.data.database.entities.IBaseEntity
import com.pedro.expresstpv.domain.model.IBaseModel
import com.pedro.expresstpv.domain.model.LineaTicket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * Repositorio base del que deben heredar el resto de repositorios
 * Aqui definiremos el sistema de cache para obtimizar la aplicacion y las funciones comunes a todos los repositorios
 *
 * Cada Repositorio que nos herede debera de especificar cual es su objeto de Entidad. Por ejemplo LineaTicketEntity y luego su objeto de dominio como por ejemplo LineaTicket
 *
 * La clase hereda de la interfaz IBaseRepository, donde especificaremos las funciones que usaremos en la clase abstracta
 * @param dao Dao (Debe de heredar de IBaseDao) donde impelementaremos las consultas basicas
 * @param Domain Especificamos el objeto de dominio
 * @param Entity Especificamos el objeto de Room
 */
abstract class BaseRepository <Domain: IBaseModel, Entity : IBaseEntity> (
    private val dao : IBaseDao<Entity>
) : IBaseRepository<Domain> {


    private var mapDomain : HashMap<Int, Domain> = hashMapOf() //Cache de los objetos de la capa de dominio
    private var mapEntity : HashMap<Int, Entity> = hashMapOf() //Cache de los objetos de la capa de Room
    private var mapTempEntity : HashMap<Int, Entity> = hashMapOf() //Cache temporal donde compararemos los cambios para luego ir limpiandola


    private val entityFlow: Flow<List<Entity>> = dao.getAll() //Flow obtenido desde la base de datos
    private val domainFlow : Flow<List<Domain>> = entityFlow
        .onEach {
            loadCache(it)

        }
        .map {
            mapDomain()
            mapDomain.values.toList()
        }
        .flowOn(Dispatchers.IO)


    /**
     * Comparara la lista obtenida del flow de room para saber donde deberemos aplicar cambios.
     * Todos los cambios lo escribiremos en la cache temporal, donde luego mapearemos los objetos que no
     * teniamos correctamente cacheados
     * @param list Lista de entidades que deberemos de comprobar para cachear
     */
    protected open suspend fun loadCache(list: List<Entity>){
        //Al usar copias evitamos tener el concurrent Exception que salta a veces en la aplicacion
//        val mapEntityCopy = mapEntity.toMap()
//        val listCopy = list.toList()
        //Recorreremos la lista que nos llegue de la base de datos
        list.forEach {
            val entry = mapEntity[it.id]
            if (entry == null || entry != it){
                mapEntity[it.id] = it
                mapTempEntity[it.id] = it
            }
        }
        //Si por lo que sea la lista nos devuelve vacia deberemos de limpiar todos los mapas
        if (list.isEmpty()){
            clearMaps()
            return
        }
        //Si el diccionario tiene elementos que no se encuentran en la lista, entonces deberemos de eliminar esos elementos concretos
        val filtrado = mapEntity.values.toList().filterNot {map ->
            list.contains(map)
        }
        filtrado.forEach {
            mapEntity.remove(it.id)
            mapTempEntity.remove(it.id)
            mapDomain.remove(it.id)
        }
    }

    /**
     * Se encargara de convertir del diccionario temporal al diccionario de dominio todos los objetos.
     * Si hay objetos en el diccionario temporal es por que no estan cacheados en la capa de dominio.
     * Por ultimo limpia el diccionario temporal
     */
    private suspend fun mapDomain() = withContext(Dispatchers.Default){
        val mapTempEntityCopy = mapTempEntity.toMap()
        mapTempEntityCopy.forEach{ (key, value) ->
            val domain = toDomain(value)
            mapDomain[key] = domain
        }
        mapTempEntity.clear()
    }

    /**
     * Limpia los diccionarios, limpiando asi la cache
     */
    private fun clearMaps(){
        mapEntity.clear()
        mapTempEntity.clear()
        mapDomain.clear()
    }


    /**
     * Carga un estado inicial del flow para obtener una cache
     */
    private suspend fun loadFlow(){
       domainFlow.first()
    }

    override fun getAllFlow() = domainFlow

    override suspend fun getAll() : List<Domain> {
        loadFlow()
        return mapDomain.values.toList()
    }

    override suspend fun getById(id : Int) : Domain? {
        loadFlow()
        return mapDomain[id]
    }

    override suspend fun insert(domain: Domain) {
        dao.insert(toEntity(domain))
    }

    override suspend fun insertAll(list: List<Domain>) {
        dao.insertAll(list.map { toEntity(it) })
    }

    override suspend fun insertOrUpdate(domain: Domain) {
        dao.insertOrUpdate(toEntity(domain))
    }

    override suspend fun update(domain: Domain) {
        dao.update(toEntity(domain))
    }

    override suspend fun updateAll(list: List<Domain>) {
        dao.updateAll(list.map { toEntity(it) })
    }

    override suspend fun delete(domain: Domain){
        dao.delete(toEntity(domain))
    }
    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun deleteList(list: List<Domain>) {
        dao.deleteList(list.map { toEntity(it) })
    }

    abstract suspend fun toEntity(domain: Domain) : Entity

    abstract suspend fun toDomain(entity: Entity) : Domain
}