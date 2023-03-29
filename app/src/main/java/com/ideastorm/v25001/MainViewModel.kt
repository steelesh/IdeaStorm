package com.ideastorm.v25001

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.dto.User
import com.ideastorm.v25001.service.ActivityService
import com.ideastorm.v25001.service.IActivityService
import kotlinx.coroutines.launch

class MainViewModel(var activityService : IActivityService = ActivityService()) : ViewModel() {
    var activity : MutableLiveData<Activity> = MutableLiveData<Activity>()
    var user : User? = null

    private lateinit var firestore : FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun fetchActivity() {
        viewModelScope.launch {
            var innerActivity = activityService.fetchActivity()
            activity.postValue(innerActivity)
        }
    }

    //Still setting up user collections for database
    fun saveActivity(activity: Activity){
        firestore.collection("users")
    }

    fun saveUser() {
        user?.let {
            user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "user save successful") }
            handle.addOnFailureListener { Log.e("Firebase", "user save failed") }
        }
    }
}
