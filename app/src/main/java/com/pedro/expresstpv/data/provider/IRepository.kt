package com.pedro.expresstpv.data.provider

interface IRepository {

    suspend fun reloadFromDatabase()

}