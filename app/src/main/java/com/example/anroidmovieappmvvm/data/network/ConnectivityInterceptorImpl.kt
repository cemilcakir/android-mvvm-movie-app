package com.example.anroidmovieappmvvm.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.anroidmovieappmvvm.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptorImpl(context: Context) : ConnectivityInterceptor {

    private val appContext = context.applicationContext

    private fun isOffline() : Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOffline())
            throw NoConnectivityException()

        return chain.proceed(chain.request())
    }
}