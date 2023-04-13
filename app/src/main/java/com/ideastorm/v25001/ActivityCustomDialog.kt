package com.ideastorm.v25001

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ideastorm.v25001.ui.theme.IdeaStormTheme


@Composable
fun CustomDialog(setShowDialog: (Boolean) -> Unit) {

    IdeaStormTheme {
        Dialog(onDismissRequest = { setShowDialog(false) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colors.background
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Generated Activity:",
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "The Generated Activity",
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Button(
                                onClick = { setShowDialog(false) },
                                modifier = Modifier.height(50.dp)
                            ) {
                                Text(text = stringResource(R.string.save))
                            }

                            Button(
                                onClick = { setShowDialog(false) },
                                modifier = Modifier.height(50.dp)
                            ) {
                                Text(text = stringResource(R.string.dismiss))
                            }

                            Button(
                                onClick = { setShowDialog(false) },
                                modifier = Modifier.height(50.dp)
                            ) {
                                Text(text = "Ignore")
                            }
                        }
                    }
                }
            }
        }
    }
}