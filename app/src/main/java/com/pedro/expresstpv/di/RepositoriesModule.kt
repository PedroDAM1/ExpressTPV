package com.pedro.expresstpv.di

import com.pedro.expresstpv.data.database.dao.CategoriaDao
import com.pedro.expresstpv.data.provider.CategoriaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {
    @Singleton
    @Provides
    fun provideCategoriaRepository(categoriaDao: CategoriaDao): CategoriaRepository {
        return CategoriaRepository(categoriaDao)
    }
}