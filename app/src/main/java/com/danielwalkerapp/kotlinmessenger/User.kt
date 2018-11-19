package com.danielwalkerapp.kotlinmessenger

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val username: String, val profileImageUrl: String, val uid: String): Parcelable {
    //new Kotlin syntax
    constructor(): this("","","")
}