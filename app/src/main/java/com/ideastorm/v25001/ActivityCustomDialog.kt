package com.ideastorm.v25001

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ideastorm.v25001.dto.Activity
import com.ideastorm.v25001.service.ActivityService
import com.ideastorm.v25001.ui.theme.IdeaStormTheme
import kotlinx.coroutines.launch

@Composable
fun CustomDialog(setShowDialog: (Boolean) -> Unit) {
    val activityService = remember { ActivityService() }
    var currentActivity by remember { mutableStateOf<Activity?>(null) }

    LaunchedEffect(Unit) {
        launch {
            currentActivity = activityService.fetchActivity()
        }
    }
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
                                    text = currentActivity?.activity ?: "Loading...",
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
                            modifier = Modifier.fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Button(
                                modifier = Modifier.height(48.dp),
                                onClick = { setShowDialog(false) },
                            ) {
                                Text(
                                    text = stringResource(R.string.save),
                                )
                            }
                            Button(
                                modifier = Modifier.height(48.dp),
                                onClick = { setShowDialog(false) },
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
