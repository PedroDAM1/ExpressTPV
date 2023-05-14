package com.pedro.expresstpv.di

import com.pedro.expresstpv.data.database.dao.*
import com.pedro.expresstpv.data.provider.*
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

    @Singleton
    @Provides
    fun provideLineaTicketRepository(lineaTicketDao: LineaTicketDao, ticketRepository: TicketRepository): LineaTicketRepository {
        return LineaTicketRepository(lineaTicketDao, ticketRepository)
    }

    @Singleton
    @Provides
    fun provideCierreRepository(cierreDao: CierreDao): CierresRepository {
        return CierresRepository(cierreDao)
    }

    @Singleton
    @Provides
    fun provideMetodoPagoRepository(metodoPagoDao: MetodoPagoDao): MetodoPagoRepository {
        return MetodoPagoRepository(metodoPagoDao)
    }

    @Singleton
    @Provides
    fun provideTicketRepository(ticketDao: TicketDao, cierresRepository: CierresRepository, metodoPagoRepository: MetodoPagoRepository): TicketRepository {
        return TicketRepository(ticketDao, cierresRepository, metodoPagoRepository)
    }
}