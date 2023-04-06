package com.ideastorm.v25001.dto

import android.graphics.ColorSpace.Model
import com.google.gson.annotations.SerializedName


data class Activity(
    @SerializedName("activity")
    var activity: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("availability")
    var availability: Double = 0.0,
    @SerializedName("participants")
    var participants: Int = 0,
    @SerializedName("price")
    var price: Double = 0.0,
    @SerializedName("key")
    var key: String = ""
) {
    override fun toString(): String {
        return activity
    }

    fun  withKey( key: String): Activity {
        this.key = key
        return this
    }
}