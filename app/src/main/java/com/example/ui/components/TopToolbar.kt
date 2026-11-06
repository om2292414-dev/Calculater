package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AngleUnit
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CharcoalCard
import com.example.ui.theme.OrangeEquals
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextSecondary

@Composable
fun TopToolbar(
    isScientificExpanded: Boolean,
    angleUnit: AngleUnit,
    onToggleFullscreen: () -> Unit,
    onToggleScientific: () -> Unit,
    onOpenToolsDashboard: () -> Unit,
    onOpenHistory: () -> Unit,
    onToggleAngleUnit: () -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Expand / Fullscreen icon
        IconButton(
            onClick = onToggleFullscreen,
            modifier = Modifier.testTag("toolbar_btn_expand").size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.OpenInFull,
                contentDescription = "Expand Fullscreen or History",
                tint = PureWhite,
                modifier = Modifier.size(22.dp)
            )
        }

        // 2. Scientific functions icon
        IconButton(
            onClick = onToggleScientific,
            modifier = Modifier.testTag("toolbar_btn_scientific").size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Calculate,
                contentDescription = "Scientific Functions",
                tint = if (isScientificExpanded) OrangeEquals else PureWhite,
                modifier = Modifier.size(24.dp)
            )
        }

        // 3. Calculator tools / grid icon
        IconButton(
            onClick = onOpenToolsDashboard,
            modifier = Modifier.testTag("toolbar_btn_tools").size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.GridView,
                contentDescription = "Tools Dashboard",
                tint = PureWhite,
                modifier = Modifier.size(24.dp)
            )
        }

        // 4. Three-dot menu icon
        Box {
            IconButton(
                onClick = { menuExpanded = true },
                modifier = Modifier.testTag("toolbar_btn_menu").size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "More Options",
                    tint = PureWhite,
                    modifier = Modifier.size(24.dp)
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier
                    .background(CharcoalCard, RoundedCornerShape(12.dp))
                    .padding(vertical = 4.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("Calculation History", color = PureWhite, fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(Icons.Outlined.History, contentDescription = null, tint = OrangeEquals)
                    },
                    onClick = {
                        menuExpanded = false
                        onOpenHistory()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Angle Unit: ${if (angleUnit == AngleUnit.DEG) "DEG (Degrees)" else "RAD (Radians)"}",
                            color = PureWhite,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Outlined.Tune, contentDescription = null, tint = PureWhite)
                    },
                    onClick = {
                        menuExpanded = false
                        onToggleAngleUnit()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Tools Dashboard", color = PureWhite, fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(Icons.Outlined.GridView, contentDescription = null, tint = PureWhite)
                    },
                    onClick = {
                        menuExpanded = false
                        onOpenToolsDashboard()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Clear All", color = PureWhite, fontSize = 14.sp) },
                    onClick = {
                        menuExpanded = false
                        onClearAll()
                    }
                )
            }
        }
    }
}
