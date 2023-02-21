package com.ideastorm.v25001

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ideastorm.v25001.ui.theme.IdeaStormTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IdeaStormTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                }
                OptionMenu("IdeaStorm")
            }
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
        title = { Text(appName)},
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.MoreVert, "")
            }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false}) {
                DropdownMenuItem(onClick = { Toast.makeText(context, "Account", Toast.LENGTH_SHORT).show() }) {
                    Text(text = "Account")
                }
                DropdownMenuItem(onClick = { Toast.makeText(context, "Exit", Toast.LENGTH_SHORT).show() }) {
                    Text(text = "Exit")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IdeaStormTheme {
        OptionMenu("IdeaStorm")
    }
}