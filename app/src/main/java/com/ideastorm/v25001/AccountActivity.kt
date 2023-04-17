package com.ideastorm.v25001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                ProfileScreen()
            }
        }
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
