package com.example.moviecatalog.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var name : String,
    var image : Int,
    var overview : String
) : Parcelable