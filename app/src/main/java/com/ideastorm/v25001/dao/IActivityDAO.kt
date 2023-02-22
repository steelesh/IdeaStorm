package com.ideastorm.v25001.dao

import com.ideastorm.v25001.dto.Activity
import retrofit2.Call
import retrofit2.http.GET

interface IActivityDAO {
    @GET("/activity")
    fun getActivity() : Call<Activity>
}
