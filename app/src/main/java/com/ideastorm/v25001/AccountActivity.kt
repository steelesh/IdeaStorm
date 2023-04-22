package com.ideastorm.v25001

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ideastorm.v25001.dto.Photo
import com.ideastorm.v25001.dto.User
import com.ideastorm.v25001.ui.theme.IdeaStormTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp

class AccountActivity : ComponentActivity() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private var strUri by mutableStateOf("")
    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            firebaseUser?.let {
                val user = User(it.uid, it.displayName)
                viewModel.user = user
                viewModel.retrieveProfilePicture()
            }
            IdeaStormTheme {
                OptionMenu("IdeaStorm")
                ProfileScreen()
            }
        }
    }

    @Composable
    fun OptionMenu(appName: String) {
        var showMenu by remember { mutableStateOf(false) }
        val context = LocalContext.current
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    appName,
                    modifier = Modifier.clickable {
                        val mainIntent = Intent(context, MainActivity::class.java)
                        context.startActivity(mainIntent)
                    }
                )
            },
            backgroundColor = MaterialTheme.colors.primary,
            actions = {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Default.MoreVert, stringResource(R.string.Navigation))
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(onClick = {
                        val mainIntent = Intent(context, MainActivity::class.java)
                        context.startActivity(mainIntent)
                    }) {
                        Text(text = stringResource(R.string.Home))
                    }
                }
            }
        )
    }

    @Composable
    fun ProfileScreen() {
        val firestore = Firebase.firestore
        val user = firebaseUser
        val savedActivities = remember { mutableStateOf(listOf<DocumentSnapshot>()) }
        val showSavedActivities = remember { mutableStateOf(false) }
        val ignoredActivities = remember { mutableStateOf(listOf<DocumentSnapshot>()) }
        val showIgnoredActivities = remember { mutableStateOf(false) }

        LaunchedEffect(user) {
            user?.let {
                fetchSavedActivities(user, savedActivities)
                fetchIgnoredActivities(user, ignoredActivities)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 63.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ProfileImage()
                firebaseUser?.let { user ->
                    user.displayName?.let { name ->
                        Text(
                            text = name,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h4
                        )
                    }
                    user.email?.let { email ->
                        Text(
                            text = email,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .padding(top = 56.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .padding(top = 150.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Saved Activities (${savedActivities.value.size})",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(top = 9.dp, start = 10.dp)
                            )
                            IconButton(onClick = {
                                showSavedActivities.value = !showSavedActivities.value
                            }) {
                                Icon(
                                    if (showSavedActivities.value) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                                    contentDescription = "Toggle Ignored Activities List"
                                )
                            }
                        }
                        if (showSavedActivities.value) {
                            val openDialog = remember { mutableStateOf(false) }
                            val selectedActivity =
                                remember { mutableStateOf<DocumentSnapshot?>(null) }

                            LazyColumn(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                            ) {
                                items(savedActivities.value) { document ->
                                    val savedActivity =
                                        document.get("activity")?.toString() ?: "No activity"
                                    Card(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .fillMaxWidth(),
                                        backgroundColor = MaterialTheme.colors.surface,
                                        shape = RoundedCornerShape(4.dp),
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .border(
                                                    BorderStroke(1.dp, Color.Black),
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .background(MaterialTheme.colors.surface),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = savedActivity,
                                                style = MaterialTheme.typography.body2,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(start = 8.dp)
                                            )
                                            IconButton(
                                                onClick = {
                                                    selectedActivity.value = document
                                                    openDialog.value = true
                                                },
                                                modifier = Modifier.padding(end = 8.dp)
                                            ) {
                                                Icon(
                                                    Icons.Filled.Clear,
                                                    contentDescription = "Ignore saved activity"
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                            if (openDialog.value) {
                                AlertDialog(
                                    onDismissRequest = { openDialog.value = false },
                                    title = { Text("Remove saved Activity") },
                                    text = { Text("Do you want to remove this activity from the saved list?") },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                openDialog.value = false
                                                selectedActivity.value?.let { document ->
                                                    firestore.runBatch { batch ->
                                                        batch.delete(document.reference)
                                                    }.addOnSuccessListener {
                                                        savedActivities.value =
                                                            savedActivities.value.filter { it != document }
                                                    }
                                                }
                                            }
                                        ) {
                                            Text("Yes")
                                        }
                                    },
                                    dismissButton = {
                                        Button(
                                            onClick = { openDialog.value = false }
                                        ) {
                                            Text("No")
                                        }
                                    }
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Ignored Activities (${ignoredActivities.value.size})",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(top = 9.dp, start = 10.dp)
                            )
                            IconButton(onClick = {
                                showIgnoredActivities.value = !showIgnoredActivities.value
                            }) {
                                Icon(
                                    if (showIgnoredActivities.value) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                                    contentDescription = "Toggle Ignored Activities List"
                                )
                            }
                        }
                        if (showIgnoredActivities.value) {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                            ) {
                                items(ignoredActivities.value) { document ->
                                    val ignoredActivity =
                                        document.get("activity")?.toString() ?: "No activity"
                                    val showDeleteDialog = remember { mutableStateOf(false) }
                                    if (showDeleteDialog.value) {
                                        AlertDialog(
                                            onDismissRequest = { showDeleteDialog.value = false },
                                            title = { Text(text = "Remove ignored Activity") },
                                            text = { Text(text = "Do you want to remove this activity from the ignored list?") },
                                            confirmButton = {
                                                Button(
                                                    onClick = {
                                                        showDeleteDialog.value = false
                                                        firestore.runBatch { batch ->
                                                            batch.delete(document.reference)
                                                        }.addOnSuccessListener {
                                                            ignoredActivities.value =
                                                                ignoredActivities.value.filter { it != document }
                                                        }
                                                    }
                                                ) {
                                                    Text(text = "Delete")
                                                }
                                            },
                                            dismissButton = {
                                                Button(
                                                    onClick = { showDeleteDialog.value = false }
                                                ) {
                                                    Text(text = "Cancel")
                                                }
                                            }
                                        )
                                    }
                                    Card(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .fillMaxWidth(),
                                        backgroundColor = MaterialTheme.colors.surface,
                                        shape = RoundedCornerShape(4.dp),
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .border(
                                                    BorderStroke(1.dp, Color.Black),
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .background(MaterialTheme.colors.surface),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = ignoredActivity,
                                                style = MaterialTheme.typography.body2,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(start = 8.dp)
                                            )
                                            IconButton(
                                                onClick = { showDeleteDialog.value = true },
                                                modifier = Modifier.padding(end = 8.dp)
                                            ) {
                                                Icon(
                                                    Icons.Filled.Clear,
                                                    contentDescription = "Remove ignored activity",
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ProfileImage() {
//        If has profile image display that else display icon
//        Grab image url from firebase
        LaunchedEffect(viewModel.profileImageURI) {

        }
        if (viewModel.profileImageURI.value.isNotEmpty()) {
            AsyncImage(
                model = viewModel.profileImageURI.value,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        } else {
            Icon(
                Icons.Rounded.AccountBox,
                contentDescription = stringResource(id = R.string.account),
            )
            Button(onClick = { takePhoto() }) {
                Text(
                    text = stringResource(R.string.accountButtonText),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

    }

    private fun takePhoto() {
        if (hasCameraPermission() == PERMISSION_GRANTED && hasExternalStoragePermission() == PERMISSION_GRANTED) {
            invokeCamera()
        } else {
            requestMultiplePermissionsLauncher.launch(
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA,
                )
            )
        }
    }

    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultsMap ->
        var permissionGranted = false
        resultsMap.forEach {
            if (it.value == true) {
                permissionGranted = it.value
            } else {
                permissionGranted = false
                return@forEach
            }
        }
        if (permissionGranted) {
            invokeCamera()
        } else {
            Toast.makeText(this, getString(R.string.cameraPermissionsText), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(this, "com.ideastorm.v31001.fileprovider", file)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            var foo = e.message
        }
        getCameraImage.launch(uri)
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Activity_${timestamp}",
            ".jpg",
            imageDirectory
        ).apply {
            currentImagePath = absolutePath
        }
    }

    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.i(TAG, "Image Location: $uri")
                strUri = uri.toString()
                val photo = Photo(localUri = uri.toString())
                viewModel.photos.add(photo)
                viewModel.saveProfileImage()

            } else {
                Log.e(TAG, "Image not saved. $uri")
            }
        }

    private fun hasCameraPermission() =
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

    private fun hasExternalStoragePermission() =
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun fetchSavedActivities(
        user: FirebaseUser,
        savedActivities: MutableState<List<DocumentSnapshot>>
    ) {
        val savedActivitiesCollection = Firebase.firestore
            .collection("users")
            .document(user.uid)
            .collection("savedActivities")
        savedActivitiesCollection
            .get()
            .addOnSuccessListener { documents ->
                savedActivities.value = documents.documents
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }

    private fun fetchIgnoredActivities(
        user: FirebaseUser,
        ignoredActivities: MutableState<List<DocumentSnapshot>>
    ) {
        val ignoredActivitiesCollection = Firebase.firestore
            .collection("users")
            .document(user.uid)
            .collection("ignoredActivities")
        ignoredActivitiesCollection
            .get()
            .addOnSuccessListener { documents ->
                ignoredActivities.value = documents.documents
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }
}
