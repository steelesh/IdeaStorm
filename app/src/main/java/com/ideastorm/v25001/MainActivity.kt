package com.ideastorm.v25001

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ideastorm.v25001.dto.User
import com.ideastorm.v25001.ui.theme.IdeaStormTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * This class represents the main activity for the IdeaStorm app and sets up the UI layout and theme
 */
class MainActivity : ComponentActivity() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var selectedParticipantOption: String
    private lateinit var selectedTypeOption: String
    private lateinit var selectedPriceOption: String
    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            firebaseUser?.let {
                val user = User(it.uid, it.displayName)
                viewModel.user = user
            }
            IdeaStormTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.background
                ) {
                }
                OptionMenu(getString(R.string.appName))
                Greeting(getString(R.string.greeting))
                ParticipantsSpinner()
                ActivityTypeSpinner()
                PriceSpinner()
                GenerateActivityButton()
            }
        }
    }
    /**
     * Creates a TopAppBar with the app title on the left and a navigation menu on the right
     * @author Steele Shreve
     * @param appName name of the application
     */
    @Composable
    fun OptionMenu(appName: String) {
        var showMenu by remember { mutableStateOf(false) }
        var showAccount by remember { mutableStateOf(false) }
        val context = LocalContext.current
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    appName,
                    modifier = Modifier.clickable {
                        if (context !is MainActivity) {
                            val mainIntent = Intent(context, MainActivity::class.java)
                            context.startActivity(mainIntent)
                        }
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
                        if (viewModel.user != null) {
                            showAccount = !showAccount
                            val accountIntent = Intent(this@MainActivity, AccountActivity::class.java)
                            startActivity(accountIntent)
                        } else {
                            signIn()
                        }
                    }) {
                        Text(text = stringResource(R.string.account))
                    }
                }
            }
        )
    }
    /**
     * Creates a message greeting the user with a friendly message.
     * @author Steele Shreve
     * @param greeting what message displays to user
     */
    @Composable
    fun Greeting(greeting: String) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 56.dp)
                    .fillMaxWidth()
                    .height(128.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = greeting,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
    /**
     * Creates a dropdown menu with options for selecting the number of participants
     * @author Steele Shreve
     */
    @Composable
    fun ParticipantsSpinner() {
        val participantOptions = listOf("One person", "Two person", "Group")
        var participantText by remember { mutableStateOf("Number of participants") }
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                .padding(top = 192.dp)
                    .width(250.dp)
                    .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(4.dp))
                    .padding(16.dp)
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = participantText,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    stringResource(R.string.dropdownArrow)
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    participantOptions.forEach { participantOption ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            participantText = participantOption
                            selectedParticipantOption = participantOption
                        }) {
                            Text(text = participantOption)
                        }
                    }

                }
            }
        }
    }
    /**
     * Creates a dropdown menu with options for selecting the type of activity
     * @author Steele Shreve
     */
    @Composable
    fun ActivityTypeSpinner() {
        val typeOptions = listOf(
            "Education",
            "Recreational",
            "Social",
            "Diy",
            "Charity",
            "Cooking",
            "Relaxation",
            "Music",
            "Busywork"
        )
        var typeText by remember { mutableStateOf("Activity type") }
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 292.dp)
                    .width(250.dp)
                    .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(4.dp))
                    .padding(16.dp)
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = typeText, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    stringResource(R.string.dropdownArrow)
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    typeOptions.forEach { typeOption ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            typeText = typeOption
                            selectedTypeOption = typeOption
                        }) {
                            Text(text = typeOption)
                        }
                    }

                }
            }
        }
    }
    /**
     * Creates a dropdown menu with options for selecting the price range of activity
     * @author Steele Shreve
     */
    @Composable
    fun PriceSpinner() {
        val priceOptions = listOf("Free", "Low", "High")
        var priceText by remember { mutableStateOf("Price range") }
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 392.dp)
                    .width(250.dp)
                    .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(4.dp))
                    .padding(16.dp)
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = priceText, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    stringResource(R.string.dropdownArrow)
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    priceOptions.forEach { priceOption ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            priceText = priceOption
                            selectedPriceOption = priceOption
                        }) {
                            Text(text = priceOption)
                        }
                    }
                }
            }
        }
    }
    /**
     * Creates a button that generates an activity with respect to the user-specified filters
     * @author Steele Shreve
     */
    @Composable
    fun GenerateActivityButton() {
        var showLoader by remember { mutableStateOf(false) }
        val openDialog = remember { mutableStateOf(false) }
        if(openDialog.value)
            CustomDialog(setShowDialog = { openDialog.value = it  } )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 492.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        showLoader = !showLoader
                        openDialog.value = true
                        viewModel.fetchActivity(
                            selectedParticipantOption,
                            selectedPriceOption,
                            selectedTypeOption
                        )
                    },
                    modifier = Modifier
                        .width(250.dp)
                        .height(128.dp)
                        .border( width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp))
                ) {
                    Text(
                        text = stringResource(R.string.buttonText),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    }
    @Composable
    fun CustomDialog(setShowDialog: (Boolean) -> Unit) {
        val currentActivity by viewModel.activity.observeAsState()
        IdeaStormTheme {
            Dialog(onDismissRequest = { setShowDialog(false) }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colors.background,
                ) {
                    Box(
                        modifier = Modifier.height(225.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(modifier = Modifier.padding(bottom = 16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier.padding(top = 12.dp, start = 8.dp),
                                    text = "Activity:",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.subtitle1,
                                    color = Color.Gray
                                )
                                IconButton(onClick = { setShowDialog(false) }) {
                                    Icon(
                                        modifier = Modifier.width(24.dp),
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Close",
                                        tint = Color.Gray
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 8.dp),
                                ) {
                                    Text(
                                        text = currentActivity?.toString() ?: "Loading...",
                                        style = MaterialTheme.typography.h6,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Button(
                                    modifier = Modifier.height(48.dp),
                                    onClick = {
                                        if (currentActivity != null) {
                                            if (viewModel.user != null) {
                                                viewModel.saveActivity(currentActivity!!)
                                            } else {
                                                signIn()
                                            }
                                        }
                                        setShowDialog(false)
                                              },
                                ) {
                                    Text(
                                        text = stringResource(R.string.save),
                                    )
                                }
                                Button(
                                    modifier = Modifier.height(48.dp),
                                    onClick = {
                                        if (currentActivity != null) {
                                            if (viewModel.user != null) {
                                                viewModel.ignoreActivity(currentActivity!!)
                                            } else {
                                                signIn()
                                            }
                                        }
                                        setShowDialog(false)
                                              },
                                ) {
                                    Text(
                                        text = stringResource(R.string.ignore),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        val signInIntent =
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .build()
        signInLauncher.launch(signInIntent)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.signInResult(res)
    }
    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                val user = User(it.uid, it.displayName)
                viewModel.user = user
                viewModel.saveUser()
            }
        } else {
            Log.e("MainActivity.kt", "Error Logging in " + response?.error?.errorCode)
        }
    }
}
