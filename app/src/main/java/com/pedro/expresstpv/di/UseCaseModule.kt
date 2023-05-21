package com.pedro.expresstpv.di

import com.pedro.expresstpv.data.provider.ArticuloRepository
import com.pedro.expresstpv.data.provider.LineaTicketRepository
import com.pedro.expresstpv.data.provider.TicketRepository
import com.pedro.expresstpv.data.usecase.ArticulosUseCase
import com.pedro.expresstpv.data.usecase.LineaTicketUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideLineaTicketUseCase(lineaTicketRepository: LineaTicketRepository, ticketRepository: TicketRepository) :LineaTicketUseCases {
        return LineaTicketUseCases(lineaTicketRepository, ticketRepository)
    }

    @Singleton
    @Provides
    fun provideArticuloUseCase(articuloRepository: ArticuloRepository) : ArticulosUseCase{
        return ArticulosUseCase(articuloRepository)
    }

}