package com.ideastorm.v25001

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ideastorm.v25001.ui.theme.IdeaStormTheme

class AccountActivity : ComponentActivity() {
    var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IdeaStormTheme {
                OptionMenu("IdeaStorm")
                ProfileScreen()
            }
        }
    }
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
                    DropdownMenuItem(onClick = {
                        Toast.makeText(context, R.string.exit, Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = stringResource(R.string.exit))
                    }
                }
            }
        )
    }
    @Composable
    fun ProfileScreen() {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            firebaseUser?.let { user ->
                user.displayName?.let { name ->
                    Text(
                        text = name,
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center
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
        }
    }
}
