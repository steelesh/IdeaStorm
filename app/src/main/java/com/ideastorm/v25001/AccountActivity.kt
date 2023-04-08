package com.ideastorm.v25001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ideastorm.v25001.ui.theme.IdeaStormTheme

class AccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IdeaStormTheme {
                ProfileScreen()
            }
        }
    }

    @Composable
    fun ProfileScreen() {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("User Profile")
        }
    }
}
