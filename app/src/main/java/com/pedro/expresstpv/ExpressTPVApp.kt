package com.pedro.expresstpv

import android.app.Application
import com.pedro.expresstpv.data.usecase.LineaTicketUseCases
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ExpressTPVApp : Application()