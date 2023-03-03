package com.ideastorm.v25001

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.service.ActivityService
import com.ideastorm.v25001.service.IActivityService
import kotlinx.coroutines.launch

class MainViewModel(var activityService : IActivityService = ActivityService()) : ViewModel() {
    var activity : MutableLiveData<Activity> = MutableLiveData<Activity>()

    private lateinit var firestore : FirebaseFirestore

    fun fetchActivity() {
        viewModelScope.launch {
            var innerActivity = activityService.fetchActivity()
            activity.postValue(innerActivity)
        }
    }
}
