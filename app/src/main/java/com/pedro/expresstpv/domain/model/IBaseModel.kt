package com.pedro.expresstpv.domain.model

/**
 * Todos los objetos de dominio deberan de heredar de esta clase para el control correcto de ellos
 */
interface IBaseModel : java.io.Serializable {
    //Todos las clases que hereden, deberan de usar este atributo
    val id : Int

}