//package com.example.authfirebase.data.network
//
//import io.reactivex.Flowable
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//interface api {
//    @GET(Constants.GET_DIRECTIONS)
//    fun getDirections(
//        @Query("origin") origin: String,
//        @Query("destination") destination: String,
//        @Query("mode") mode: String
//    ): Flowable<DirectionsResponse>
//}