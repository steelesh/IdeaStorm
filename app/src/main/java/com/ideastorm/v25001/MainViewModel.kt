package com.ideastorm.v25001

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.dto.User
import com.ideastorm.v25001.service.ActivityService
import com.ideastorm.v25001.service.IActivityService

class MainViewModel(var activityService: IActivityService = ActivityService()) : ViewModel() {
    var activity: MutableLiveData<Activity> = MutableLiveData<Activity>()
    var user: User? = null

    private val TAG = "Activity Retrieval"
    private lateinit var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun fetchActivity(
        participantsSelection: String = "One person",
        priceSelection: String = "Free",
        typeSelection: String = "Cooking"
    ) {
        var queryBulder = firestore.collection("activities")
            .whereEqualTo("type", typeSelection.lowercase())

        when (participantsSelection) {
            "One person" -> queryBulder = queryBulder.whereEqualTo("participants", 1)
            "Two person" -> queryBulder = queryBulder.whereEqualTo("participants", 2)
            "Group" -> queryBulder = queryBulder.whereGreaterThanOrEqualTo("participants", 3)
        }
        when (priceSelection) {
            "Free" -> queryBulder = queryBulder.whereEqualTo("price", 0)
            "Low" -> queryBulder =
                queryBulder.whereGreaterThan("price", 0).whereLessThan("price", .5)
            "High" -> queryBulder = queryBulder.whereGreaterThanOrEqualTo("price", .5)
        }

        queryBulder.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    //Still setting up user collections for database
    fun saveActivity(activity: Activity) {
        firestore.collection("users")
    }

    fun saveUser() {
        user?.let { user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "user save successful") }
            handle.addOnFailureListener { Log.e("Firebase", "user save failed") }
        }
    }
}
