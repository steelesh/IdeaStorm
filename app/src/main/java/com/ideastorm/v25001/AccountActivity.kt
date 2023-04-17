package com.ideastorm.v25001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
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
    }
}
