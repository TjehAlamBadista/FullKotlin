//package com.example.authfirebase.data.network
//
//import com.google.android.gms.common.api.Api
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import retrofit2.converter.moshi.MoshiConverterFactory
//import timber.log.Timber
//import java.util.concurrent.TimeUnit
//
//object ApiService {
//
//    private lateinit var apiService: Api
//
//    fun getService(): Api {
//        if (!::apiService.isInitialized) {
//            val retrofit = Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(MoshiConverterFactory.create())
//                .client(createOkHttpClient())
//                .build()
//
//            apiService = retrofit.create(Api::class.java)
//        }
//
//        return apiService
//    }
//}