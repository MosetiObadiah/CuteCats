package com.moseti.cutecats.ui.screens

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moseti.cutecats.R
import com.moseti.cutecats.ui.viewmodels.CatViewModel

@Composable
fun SettingsScreen(catViewModel: CatViewModel, modifier: Modifier = Modifier) {
    val state = LazyListState()

    LazyColumn (
        state = state,
        modifier = modifier.fillMaxSize()
    ) {
        item {
            SettingsItem("Theme")
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_colors_24),
                    contentDescription = null
                )

                OutlinedButton(
                    onClick = {}
                ) {
                    Text(
                        text = "Light"
                    )
                }

                OutlinedButton(
                    onClick = {}
                ) {
                    Text(
                        text = "Dark"
                    )
                }

                OutlinedButton(
                    onClick = {}
                ) {
                    Text(
                        text = "Black"
                    )
                }
            }
        }

        item {
            SettingsItem("Data")
        }
    }
}

@Composable
fun SettingsItem(sectionTitle: String) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 5.dp, start = 3.dp, end = 3.dp)
    ) {
        Text(
            text = sectionTitle,
            color = MaterialTheme.colorScheme.primary
        )
    }

    Spacer(Modifier.height(8.dp))

    CustomButtonWithToggle(
        selectedIconResId = R.drawable.outline_wand_stars_24,
        toggleLabel = "Follow System"
    ) {
        Toast.makeText(context, "$sectionTitle clicked", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun CustomButtonWithToggle(
    @DrawableRes selectedIconResId: Int,
    toggleLabel: String,
    toggleFunction: () -> Unit, ) {
    var checked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = {checked = !checked}
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(selectedIconResId),
            contentDescription = null
        )

        Spacer(Modifier.width(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth()
                .weight(1f),
            text = toggleLabel
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )

    }
}