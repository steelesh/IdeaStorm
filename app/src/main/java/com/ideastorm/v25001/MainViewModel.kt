package com.ideastorm.v25001

import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.dto.Photo
import com.ideastorm.v25001.dto.User
import com.ideastorm.v25001.service.ActivityService
import com.ideastorm.v25001.service.IActivityService

class MainViewModel(var activityService: IActivityService = ActivityService()) : ViewModel() {
    val photos: ArrayList<Photo> = ArrayList<Photo>()
    var activity: MutableLiveData<Activity> = MutableLiveData<Activity>()
    var user: User? = null

    private val TAG = "Activity Retrieval"
    private lateinit var firestore: FirebaseFirestore

    private var storageReference = FirebaseStorage.getInstance().getReference()

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun fetchActivity(
        participantsSelection: String = "Group",
        priceSelection: String = "Free",
        typeSelection: String = "Social"
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
                    activity.value = document.toObject(Activity::class.java).withKey(document.id)
                    Log.d(TAG, "${activity.value}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun saveActivity(activity: Activity) {
        user?.uid?.let {
            firestore.collection("users").document(it).collection("savedActivities")
                .document(activity.key).set(activity)
        }
    }

    fun ignoreActivity(activity: Activity) {
        user?.uid?.let {
            firestore.collection("users").document(it).collection("ignoredActivities")
                .document(activity.key).set(activity)
        }
    }

    //    TODO: Need some option to pass here whether or not they want to take a picture to save
    fun completedActivity(activity: Activity) {
        user?.uid?.let {
            val handle =
                firestore.collection("users").document(it).collection("completedActivities")
                    .document(activity.key).set(activity)
            handle.addOnSuccessListener {
                Log.d("Firebase", "user save successful")
                if (photos.isNotEmpty()) {
                    uploadPhotos(activity)
                }
            }
            handle.addOnFailureListener { Log.e("Firebase", "user save failed") }
        }
    }

    private fun uploadPhotos(activity: Activity) {
        photos.forEach { photo ->
            var uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("images/${user?.uid}/${uri.lastPathSegment}")
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(TAG, "Image uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener { remoteUri ->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo, activity)
                }
            }
            uploadTask.addOnFailureListener {
                Log.e(TAG, it.message ?: "No Message")
            }
        }
    }

    private fun updatePhotoDatabase(photo: Photo, activity: Activity) {
        user?.let {
            user ->
            var photosCollection =
                firestore.collection("users").document(user.uid).collection("completedActivities")
                    .document(activity.key).collection("photos")
            var handle = photosCollection.add(photo)
            handle.addOnSuccessListener {
                Log.d("Firebase", "user save successful")
                photo.id = it.id
                firestore.collection("users").document(user.uid).collection("completedActivities")
                    .document(activity.key).collection("photos").document(photo.id).set(photo)

            }
            handle.addOnFailureListener {
                Log.e("Firebase", "Error updating photo data: ${it.message}")
            }
        }
    }

    fun saveUser() {
        user?.let { user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "user save successful") }
            handle.addOnFailureListener { Log.e("Firebase", "user save failed") }
        }
    }
}
