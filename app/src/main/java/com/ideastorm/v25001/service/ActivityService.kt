package com.ideastorm.v25001.service

import com.ideastorm.v25001.RetrofitClientInstance
import com.ideastorm.v25001.dao.IActivityDAO
import com.ideastorm.v25001.dto.Activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

interface IActivityService {
    suspend fun fetchActivity(): Activity
}

class ActivityService : IActivityService {
    override suspend fun fetchActivity(): Activity {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IActivityDAO::class.java)
            val activity = async { service?.getActivity() }
            var result = activity.await()?.awaitResponse()?.body()
            return@withContext result!!
        }
    }
}
