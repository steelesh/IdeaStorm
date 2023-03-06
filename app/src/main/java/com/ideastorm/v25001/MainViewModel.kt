package com.ideastorm.v25001

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.service.ActivityService
import com.ideastorm.v25001.service.IActivityService
import kotlinx.coroutines.launch

class MainViewModel(var activityService : IActivityService = ActivityService()) : ViewModel() {
    private var activity : MutableLiveData<Activity> = MutableLiveData<Activity>()

    fun fetchActivity() {
        try {
            viewModelScope.launch {
                var innerActivity = activityService.fetchActivity()
                activity.postValue(innerActivity)
            }
        } catch (e: Exception) {
            throw IllegalArgumentException(e)
        }

    }
}
