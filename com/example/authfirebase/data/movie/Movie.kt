package com.example.authfirebase.data.movie

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id : String ?,

    val title : String?,

    val poster_path : String?,

    val release_date : String?

) : Parcelable {
    constructor() : this("", "", "", "")
}