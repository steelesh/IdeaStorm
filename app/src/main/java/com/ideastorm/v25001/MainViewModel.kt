package com.ideastorm.v25001

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.dto.Photo
import com.ideastorm.v25001.dto.User
import com.ideastorm.v25001.service.ActivityService
import com.ideastorm.v25001.service.IActivityService
import kotlinx.coroutines.launch

class MainViewModel(var activityService: IActivityService = ActivityService()) : ViewModel() {
    var activity: MutableLiveData<Activity> = MutableLiveData<Activity>()
    var user: User? = null
    var profileImageURI = mutableStateOf("")
    val photos: ArrayList<Photo> = ArrayList<Photo>()

    private val TAG = "Activity Retrieval"
    private lateinit var firestore: FirebaseFirestore
    private var storageReference = FirebaseStorage.getInstance().getReference()

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

    fun fetchActivity(
        participantsSelection: String,
        priceSelection: String,
        typeSelection: String
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
                if (documents.size() > 0) {
                    val document = documents.first()
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val fetchedActivity =
                        document.toObject(Activity::class.java).withKey(document.id)
                    activity.value = fetchedActivity
                    Log.d(TAG, "Fetched activity: $fetchedActivity")
                } else {
                    Log.d(TAG, "No documents found")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun saveActivity(activity: Activity) {
        Log.i(TAG, "User $user")
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

    fun retrieveProfilePicture() {
        user?.uid?.let { uid ->
            val handle = firestore.collection("users").document(uid).collection("profilePicture")
                .document("photo").get()
            handle.addOnSuccessListener { photo ->
                if (photo.exists()) {
                    photo.get("remoteUri").let {
                        profileImageURI.value = it.toString()
                    }
                } else {
                    Log.d(TAG, "No profile picture found")
                }
            }
            handle.addOnFailureListener {
                Log.e(TAG, "Could not retrieve doc $it")
            }
        }

    }

    fun saveProfileImage() {
        if (photos.isNotEmpty()) {
            uploadPhotos()
        }
    }

    private fun uploadPhotos() {
        photos.forEach { photo ->
            var uri = Uri.parse(photo.localUri)
            Log.i(TAG, "Image uploaded ${user?.uid}")
            val imageRef = storageReference.child("images/${user?.uid}/${uri.lastPathSegment}")
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(TAG, "Image uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener { remoteUri ->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo)
                }
            }
            uploadTask.addOnFailureListener {
                Log.e(TAG, it.message ?: "No Message")
            }
        }
    }

    private fun updatePhotoDatabase(photo: Photo) {
        user?.let { user ->
            firestore.collection("users").document(user.uid).collection("profilePicture")
                .document("photo").set(photo)
            profileImageURI.value = photo.remoteUri
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
