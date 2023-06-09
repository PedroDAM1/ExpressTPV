package com.pedro.expresstpv.data.database.entities

/**
 * Entidad comun que tendran todas  las demas entidades de room, de esta forma tenemos un mejor control
 * sobre la herencia
 */
interface IBaseEntity {

    //Variable comun que deben de tener todos las entidades de room
    val id : Int

}