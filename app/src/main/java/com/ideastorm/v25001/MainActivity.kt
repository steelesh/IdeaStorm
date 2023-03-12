package com.ideastorm.v25001

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.ui.theme.IdeaStormTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
var selectedParticipantOption: String? = null
var selectedTypeOption: String? = null
var selectedPriceOption: String? = null

/**
 * This class represents the main activity for the IdeaStorm app and sets up the UI layout and theme
 */
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchActivity()
            val activity by viewModel.activity.observeAsState(initial = emptyList<Activity>())
            IdeaStormTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
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
        val context = LocalContext.current
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = { Text(appName) },
            backgroundColor = Color(android.graphics.Color.parseColor("#D9D9D9")),
            actions = {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Default.MoreVert, "Navigation")
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(onClick = { signIn() }) {
                        Text(text = "Account")
                    }
                    DropdownMenuItem(onClick = {
                        Toast.makeText(context, "Exit", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "Exit")
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
                    .border(BorderStroke(1.dp, Color.Black))
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
                Icon(imageVector = Icons.Filled.ArrowDropDown, "Dropdown arrow")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    participantOptions.forEach { participantOption ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            participantText = participantOption
                        }) {
                            Text(text = participantOption)
                            selectedParticipantOption = participantOption
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
                    .border(BorderStroke(1.dp, Color.Black))
                    .padding(16.dp)
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = typeText, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, "Dropdown arrow")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    typeOptions.forEach { typeOption ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            typeText = typeOption
                        }) {
                            Text(text = typeOption)
                            selectedTypeOption = typeOption
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
                    .border(BorderStroke(1.dp, Color.Black))
                    .padding(16.dp)
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = priceText, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, "Dropdown arrow")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    priceOptions.forEach { priceOption ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            priceText = priceOption
                        }) {
                            Text(text = priceOption)
                            selectedPriceOption = priceOption
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
        var isButtonEnabled by remember { mutableStateOf(true) }
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
                    enabled = isButtonEnabled,
                    onClick = { showLoader = !showLoader; isButtonEnabled = false },
                    modifier = Modifier
                        .width(250.dp)
                        .height(128.dp)
                ) {
                    Text(
                        text = stringResource(R.string.buttonText),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
        if (showLoader) {
            DisplayLoader()
        }
    }

    @Composable
    fun DisplayLoader() {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 592.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
            }
        }

    }

    /**
     * Displays a preview for our layout in the IDE without AVD
     * @author Steele Shreve
     */
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        IdeaStormTheme {
        }
    }

    fun signIn() {
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
            user = FirebaseAuth.getInstance().currentUser
        } else {
            Log.e("MainActivity.kt", "Error Logging in " + response?.error?.errorCode)
        }
    }
}

