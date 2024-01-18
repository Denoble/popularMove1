package com.gevcorst.popular_movies_in_theaters.repository.services

import com.gevcorst.popular_movies_in_theaters.BuildConfig
import com.gevcorst.popular_movies_in_theaters.Model.MovieData
import com.gevcorst.popular_movies_in_theaters.Model.MovieReview
import com.gevcorst.popular_movies_in_theaters.Model.Review
import com.gevcorst.popular_movies_in_theaters.Model.Videos
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File
import java.util.concurrent.TimeUnit


private const val BASE_URL = "http://api.themoviedb.org/"
private const val CACHE_SIZE = 5 * 1024 * 1024L
private const val HEADER_CACHE_CONTROL = "cache-control"
private const val NO_CACHE_CONTROL = "no-cache"
val cacheDirectory = File("src/main/assets/cache")

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(okhttpClient())
    .baseUrl(BASE_URL)
    .build()

class OAuthAndCacheInterceptor(
    private val tokenType: String,
    private val acceessToken: String
) : Interceptor {
    override fun intercept(chain: Chain): okhttp3.Response {
        var request = chain.request()
        if(request.header("No-Authentication")==null){
            request = request.newBuilder().addHeader("Authorization",
                "$tokenType $acceessToken")
                .build()
        }
        val useCacheData = request.header(HEADER_CACHE_CONTROL) != NO_CACHE_CONTROL
        val initialResponse = chain.proceed(request)
        return if (!useCacheData) {
            initialResponse
        } else {
            val cacheControl = CacheControl.Builder()
                .maxAge(10, TimeUnit.MINUTES)
            initialResponse.newBuilder()
                .header("Authorization", "$tokenType $acceessToken")
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
       //return chain.proceed(request)
    }
}

/**
 * Initialize OkhttpClient with our interceptor
 */
private fun okhttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(
            OAuthAndCacheInterceptor(
                "Bearer ",
                BuildConfig.THEMOVIEDB_BEARER
            )
        )
        .addInterceptor(loggingInterceptor())
        .cache(httpCache())
        .build()
}

private fun httpCache(): Cache {
    return Cache(cacheDirectory, CACHE_SIZE)
}

private fun loggingInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.HEADERS
}

interface MovieDBService {
    @GET("3/movie/popular")
    fun getPopularMovie(@Query("language")lan:String = "en-US",
                     @Query("page")page:String ="10"): Deferred<MovieData>
    @GET("3/movie/now_playing")
    fun getNowPlaying(@Query("language")lan:String = "en-US",
                      @Query("page")page:String ="1"):Deferred<MovieData>
    @GET("3/movie/top_rated")
    fun getTopRated(@Query("language")lan:String = "en-US",
                    @Query("page")page:String ="1"):Deferred<MovieData>
    @GET("3/movie/upcoming")
    fun getUpcoming(@Query("language")lan:String = "en-US",
                    @Query("page")page:String ="1"):Deferred<MovieData>

    @GET("3/movie/{movie_id}/reviews" )
    fun getReviews(@Path("movie_id")id:Int,
                   @Query("language")lan:String = "en-US"):Deferred<Review>
    @GET("3/movie/{movie_id}/videos")
    fun getVideos(
        @Path("movie_id")id:Int,
        @Query("language")lan:String = "en-US"):Deferred<Videos>
}
object MovieDbRESTService{
    val movieDBObject:MovieDBService by lazy {
        retrofit.create(MovieDBService::class.java)
    }
}