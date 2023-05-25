package com.pedro.expresstpv.di

import com.pedro.expresstpv.data.provider.*
import com.pedro.expresstpv.data.usecase.*
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

    @Singleton
    @Provides
    fun provideTicketUseCase(ticketRepository: TicketRepository, lineaTicketUseCases: LineaTicketUseCases, cierreUseCase: CierreUseCase) : TicketUseCase {
        return TicketUseCase(ticketRepository, lineaTicketUseCases, cierreUseCase)
    }

    @Singleton
    @Provides
    fun provideMetodoPago(metodoPagoRepository: MetodoPagoRepository) : MetodoPagoUseCase{
        return MetodoPagoUseCase(metodoPagoRepository)
    }

    @Singleton
    @Provides
    fun provideCierreUseCase (cierresRepository: CierresRepository) : CierreUseCase{
        return CierreUseCase(cierresRepository)
    }

}