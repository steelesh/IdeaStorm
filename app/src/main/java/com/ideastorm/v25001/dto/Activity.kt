package com.ideastorm.v25001.dto

import com.google.gson.annotations.SerializedName

data class Activity(@SerializedName("activity")
                    var activity: String,
                    @SerializedName("accessibility")
                    var accessibility: Float,
                    @SerializedName("type")
                    var type: String,
                    @SerializedName("participants")
                    var participants: Int,
                    @SerializedName("price")
                    var price: Float,
                    @SerializedName("link")
                    var link: String,
                    @SerializedName("key")
                    var key: Long) {
    override fun toString(): String {
        return activity
    }
}