package com.example.anroidmovieappmvvm.data.network

import com.example.anroidmovieappmvvm.data.models.DetailModel
import com.example.anroidmovieappmvvm.data.models.ResponseModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val BASE_URL = "https://api.themoviedb.org/3/"
private val API_KEY = "3b80aa6376c8b4cda58d04c4263f7e47"

private val DEFAULT_LANGUAGE = "en-US"

interface MovieWebService {

    //https://api.themoviedb.org/3/movie/now_playing?api_key=3b80aa6376c8b4cda58d04c4263f7e47&language=en-US&page=1
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("page") page: Int = 1
    ): ResponseModel

    //https://api.themoviedb.org/3/movie/top_rated?api_key=3b80aa6376c8b4cda58d04c4263f7e47&language=en-US&page=1
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("page") page: Int = 1
    ): ResponseModel

    //https://api.themoviedb.org/3/movie/upcoming?api_key=3b80aa6376c8b4cda58d04c4263f7e47&language=en-US&page=1
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("page") page: Int = 1
    ): ResponseModel

    //https://api.themoviedb.org/3/movie/299534?api_key=3b80aa6376c8b4cda58d04c4263f7e47&language=en-US
    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): DetailModel

    companion object {
        operator fun invoke(): MovieWebService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(MovieWebService::class.java)
        }
    }
}
