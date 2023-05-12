package com.pedro.expresstpv.di

import com.pedro.expresstpv.data.database.dao.ArticuloDao
import com.pedro.expresstpv.data.database.dao.CategoriaDao
import com.pedro.expresstpv.data.database.dao.TipoIvaDao
import com.pedro.expresstpv.data.provider.ArticuloRepository
import com.pedro.expresstpv.data.provider.CategoriaRepository
import com.pedro.expresstpv.data.provider.TipoIvaRepository
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

    @Singleton
    @Provides
    fun provideTipoIvaRepository(tipoIvaDao: TipoIvaDao): TipoIvaRepository {
        return TipoIvaRepository(tipoIvaDao)
    }

    @Singleton
    @Provides
    fun provideArticuloRepository(articuloDao: ArticuloDao, categoriaRepository: CategoriaRepository, tipoIvaRepository: TipoIvaRepository): ArticuloRepository {
        return ArticuloRepository(articuloDao, categoriaRepository, tipoIvaRepository)
    }
}