package com.example.anroidmovieappmvvm

import android.app.Application
import com.example.anroidmovieappmvvm.data.network.ConnectivityInterceptor
import com.example.anroidmovieappmvvm.data.network.ConnectivityInterceptorImpl
import com.example.anroidmovieappmvvm.data.network.MovieWebService
import com.example.anroidmovieappmvvm.data.repository.MovieRepository
import com.example.anroidmovieappmvvm.data.repository.MovieRepositoryImpl
import com.example.anroidmovieappmvvm.ui.nowPlaying.NowPlayingFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MovieApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MovieApplication))

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { MovieWebService(instance()) }
        bind<MovieRepository>() with singleton { MovieRepositoryImpl(instance()) }
    }
}