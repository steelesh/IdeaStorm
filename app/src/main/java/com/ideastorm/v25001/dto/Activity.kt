package com.ideastorm.v25001.dto

import com.google.gson.annotations.SerializedName


/*
Activity has lot of different attributes but some of them seem unnecessary
Even though I believe that accessibility is important I think that it is somewhat implied
Also representing it as a float is somewhat confusing. Maybe changing this to be a boolean
and renaming the variable to "accessible" who more clear.
Also adding a default to participants since you're using 1 a lot in your unit tests can save you some typing
 */
data class Activity(@SerializedName("activity")
                    var activity: String,
                    /*

                    @SerializedName("accessibility")
                    var accessibility: Float,

                     */

                    @SerializedName("type")
                    var type: String,
                    @SerializedName("participants")
                    var participants: Int = 1,
                    @SerializedName("price")
                    var price: Float,
                    @SerializedName("link")
                    var link: String,

                    @SerializedName("key")
                    var key: Long)
                     {
    override fun toString(): String {
        return activity
    }
}