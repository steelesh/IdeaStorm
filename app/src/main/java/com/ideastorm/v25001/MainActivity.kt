package com.ideastorm.v25001

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ideastorm.v25001.ui.theme.IdeaStormTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IdeaStormTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                }
                OptionMenu("IdeaStorm")
                Greeting("Let's find an activity for you")
            }
        }
    }
}
/**
 * Creates a message greeting the user with a friendly message.
 * @author Steele Shreve
 * @param greeting what message displays to user
 */
@Composable
fun Greeting(greeting: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(top = 56.dp)
                .fillMaxWidth()
                .height(128.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = greeting, textAlign = TextAlign.Center, style = MaterialTheme.typography.h6)
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
        title = { Text(appName)},
        backgroundColor = Color(android.graphics.Color.parseColor("#D9D9D9")),
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.MoreVert, "Navigation")
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