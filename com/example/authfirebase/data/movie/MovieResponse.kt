package com.example.authfirebase.data.movie

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResponse(
    val results : List<Movie>

) : Parcelable {
    constructor() : this(mutableListOf())
}