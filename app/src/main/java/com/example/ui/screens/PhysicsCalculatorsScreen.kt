package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CharcoalButton
import com.example.ui.theme.CharcoalCard
import com.example.ui.theme.CharcoalCardBorder
import com.example.ui.theme.OrangeEquals
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import java.text.DecimalFormat

@Composable
fun PhysicsCalculatorsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    val modules = listOf("Kinematics", "Newton's Force", "Energy", "Ohm's Law", "Gravitation")
    var selectedModule by remember { mutableStateOf("Kinematics") }
    val df = remember { DecimalFormat("#,##0.######") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = PureWhite
                )
            }
            Text(
                text = "Physics Calculators",
                color = PureWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Module selection chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(modules) { mod ->
                val isSelected = mod == selectedModule
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedModule = mod },
                    label = {
                        Text(
                            text = mod,
                            color = if (isSelected) AmoledBlack else PureWhite,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = OrangeEquals,
                        containerColor = CharcoalButton
                    ),
                    border = null,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                when (selectedModule) {
                    "Kinematics" -> KinematicsCalculator(df)
                    "Newton's Force" -> ForceCalculator(df)
                    "Energy" -> EnergyCalculator(df)
                    "Ohm's Law" -> OhmsLawCalculator(df)
                    "Gravitation" -> GravitationCalculator(df)
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun KinematicsCalculator(df: DecimalFormat) {
    var uStr by remember { mutableStateOf("0") }
    var aStr by remember { mutableStateOf("9.8") }
    var tStr by remember { mutableStateOf("5") }

    val u = uStr.toDoubleOrNull() ?: 0.0
    val a = aStr.toDoubleOrNull() ?: 0.0
    val t = tStr.toDoubleOrNull() ?: 0.0

    val v = u + (a * t)
    val s = (u * t) + (0.5 * a * t * t)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Linear Kinematics (v = u + at, s = ut + ½at²)", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        PhysicsInput(label = "Initial Velocity (u) in m/s", value = uStr, onValueChange = { uStr = it })
        PhysicsInput(label = "Acceleration (a) in m/s²", value = aStr, onValueChange = { aStr = it })
        PhysicsInput(label = "Time (t) in seconds", value = tStr, onValueChange = { tStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Final Velocity (v)", value = "${df.format(v)} m/s")
            ResultRow(title = "Displacement (s)", value = "${df.format(s)} m")
            ResultRow(title = "Average Velocity (v_avg)", value = "${df.format((u + v) / 2.0)} m/s")
        }
    }
}

@Composable
private fun ForceCalculator(df: DecimalFormat) {
    var mStr by remember { mutableStateOf("70") }
    var aStr by remember { mutableStateOf("9.8") }
    var vStr by remember { mutableStateOf("15") }

    val m = mStr.toDoubleOrNull() ?: 0.0
    val a = aStr.toDoubleOrNull() ?: 0.0
    val v = vStr.toDoubleOrNull() ?: 0.0

    val force = m * a
    val momentum = m * v

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Newton's 2nd Law & Momentum (F = ma, p = mv)", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        PhysicsInput(label = "Mass (m) in kg", value = mStr, onValueChange = { mStr = it })
        PhysicsInput(label = "Acceleration (a) in m/s²", value = aStr, onValueChange = { aStr = it })
        PhysicsInput(label = "Velocity (v) in m/s", value = vStr, onValueChange = { vStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Net Force (F)", value = "${df.format(force)} N (Newtons)")
            ResultRow(title = "Linear Momentum (p)", value = "${df.format(momentum)} kg·m/s")
        }
    }
}

@Composable
private fun EnergyCalculator(df: DecimalFormat) {
    var mStr by remember { mutableStateOf("60") }
    var vStr by remember { mutableStateOf("10") }
    var hStr by remember { mutableStateOf("15") }

    val m = mStr.toDoubleOrNull() ?: 0.0
    val v = vStr.toDoubleOrNull() ?: 0.0
    val h = hStr.toDoubleOrNull() ?: 0.0

    val ke = 0.5 * m * v * v
    val pe = m * 9.80665 * h
    val totalEnergy = ke + pe

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Kinetic & Potential Energy (KE = ½mv², PE = mgh)", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        PhysicsInput(label = "Mass (m) in kg", value = mStr, onValueChange = { mStr = it })
        PhysicsInput(label = "Velocity (v) in m/s", value = vStr, onValueChange = { vStr = it })
        PhysicsInput(label = "Height (h) in meters", value = hStr, onValueChange = { hStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Kinetic Energy (KE)", value = "${df.format(ke)} Joules (J)")
            ResultRow(title = "Potential Energy (PE)", value = "${df.format(pe)} Joules (J)")
            ResultRow(title = "Total Mechanical Energy", value = "${df.format(totalEnergy)} Joules (J)")
        }
    }
}

@Composable
private fun OhmsLawCalculator(df: DecimalFormat) {
    var vStr by remember { mutableStateOf("12") }
    var rStr by remember { mutableStateOf("4") }

    val v = vStr.toDoubleOrNull() ?: 0.0
    val r = rStr.toDoubleOrNull() ?: 0.0

    val current = if (r != 0.0) v / r else 0.0
    val power = v * current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Ohm's Law & Electric Power (V = IR, P = VI)", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        PhysicsInput(label = "Voltage (V) in Volts", value = vStr, onValueChange = { vStr = it })
        PhysicsInput(label = "Resistance (R) in Ohms (Ω)", value = rStr, onValueChange = { rStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Current (I = V/R)", value = "${df.format(current)} A (Amperes)")
            ResultRow(title = "Power (P = VI)", value = "${df.format(power)} W (Watts)")
        }
    }
}

@Composable
private fun GravitationCalculator(df: DecimalFormat) {
    var m1Str by remember { mutableStateOf("5.972e24") }
    var m2Str by remember { mutableStateOf("70") }
    var rStr by remember { mutableStateOf("6.371e6") }

    val m1 = m1Str.toDoubleOrNull() ?: 0.0
    val m2 = m2Str.toDoubleOrNull() ?: 0.0
    val r = rStr.toDoubleOrNull() ?: 0.0

    val gConst = 6.67430e-11
    val f = if (r != 0.0) (gConst * m1 * m2) / (r * r) else 0.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Universal Gravitation (F = G·m₁·m₂ / r²)", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        PhysicsInput(label = "Mass 1 (m₁) in kg (e.g. Earth)", value = m1Str, onValueChange = { m1Str = it })
        PhysicsInput(label = "Mass 2 (m₂) in kg (e.g. Object)", value = m2Str, onValueChange = { m2Str = it })
        PhysicsInput(label = "Distance (r) in meters", value = rStr, onValueChange = { rStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Gravitational Force (F)", value = "${df.format(f)} N")
        }
    }
}

@Composable
private fun PhysicsInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = TextSecondary, fontSize = 12.sp) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CharcoalButton,
            unfocusedContainerColor = CharcoalButton,
            focusedBorderColor = OrangeEquals,
            unfocusedBorderColor = CharcoalCardBorder,
            focusedTextColor = PureWhite,
            unfocusedTextColor = PureWhite
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ResultRow(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = TextSecondary, fontSize = 14.sp)
        Text(text = value, color = OrangeEquals, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}
