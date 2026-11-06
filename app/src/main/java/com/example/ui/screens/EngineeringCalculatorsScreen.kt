package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
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
fun EngineeringCalculatorsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    val modules = listOf("Stress & Strain", "Reynolds Number", "Resistor Bands", "Parallel/Series", "Torque & Hydraulics")
    var selectedModule by remember { mutableStateOf("Stress & Strain") }
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
                text = "Engineering Suite",
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
                    "Stress & Strain" -> StressStrainCalculator(df)
                    "Reynolds Number" -> ReynoldsCalculator(df)
                    "Resistor Bands" -> ResistorColorCodeCalculator()
                    "Parallel/Series" -> ResistorNetworkCalculator(df)
                    "Torque & Hydraulics" -> TorqueHydraulicsCalculator(df)
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun StressStrainCalculator(df: DecimalFormat) {
    var fStr by remember { mutableStateOf("50000") } // 50 kN
    var aStr by remember { mutableStateOf("0.002") } // m^2
    var l0Str by remember { mutableStateOf("2.0") } // m
    var dlStr by remember { mutableStateOf("0.0005") } // m

    val f = fStr.toDoubleOrNull() ?: 0.0
    val a = aStr.toDoubleOrNull() ?: 1.0
    val l0 = l0Str.toDoubleOrNull() ?: 1.0
    val dl = dlStr.toDoubleOrNull() ?: 0.0

    val stress = if (a != 0.0) f / a else 0.0
    val strain = if (l0 != 0.0) dl / l0 else 0.0
    val youngsModulus = if (strain != 0.0) stress / strain else 0.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Stress, Strain & Young's Modulus (σ = F/A, ε = ΔL/L)", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        EngineeringInput(label = "Applied Force (F) in Newtons", value = fStr, onValueChange = { fStr = it })
        EngineeringInput(label = "Cross-sectional Area (A) in m²", value = aStr, onValueChange = { aStr = it })
        EngineeringInput(label = "Original Length (L₀) in meters", value = l0Str, onValueChange = { l0Str = it })
        EngineeringInput(label = "Deformation / Elongation (ΔL) in meters", value = dlStr, onValueChange = { dlStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Stress (σ)", value = "${df.format(stress / 1e6)} MPa (${df.format(stress)} Pa)")
            ResultRow(title = "Strain (ε)", value = String.format("%.6f", strain))
            ResultRow(title = "Young's Modulus (E)", value = "${df.format(youngsModulus / 1e9)} GPa")
        }
    }
}

@Composable
private fun ReynoldsCalculator(df: DecimalFormat) {
    var rhoStr by remember { mutableStateOf("1000") } // water density kg/m^3
    var vStr by remember { mutableStateOf("1.5") } // m/s
    var dStr by remember { mutableStateOf("0.05") } // 50mm pipe
    var muStr by remember { mutableStateOf("0.001") } // Pa.s

    val rho = rhoStr.toDoubleOrNull() ?: 1000.0
    val v = vStr.toDoubleOrNull() ?: 0.0
    val d = dStr.toDoubleOrNull() ?: 0.0
    val mu = muStr.toDoubleOrNull() ?: 0.001

    val re = if (mu != 0.0) (rho * v * d) / mu else 0.0
    val flowRegime = when {
        re < 2300 -> "Laminar Flow (Re < 2300)"
        re in 2300.0..4000.0 -> "Transitional Flow (2300 - 4000)"
        else -> "Turbulent Flow (Re > 4000)"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Reynolds Number (Re = ρ·v·D / μ)", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        EngineeringInput(label = "Fluid Density (ρ) in kg/m³", value = rhoStr, onValueChange = { rhoStr = it })
        EngineeringInput(label = "Flow Velocity (v) in m/s", value = vStr, onValueChange = { vStr = it })
        EngineeringInput(label = "Pipe Internal Diameter (D) in meters", value = dStr, onValueChange = { dStr = it })
        EngineeringInput(label = "Dynamic Viscosity (μ) in Pa·s", value = muStr, onValueChange = { muStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Reynolds Number (Re)", value = df.format(re))
            ResultRow(title = "Flow Regime", value = flowRegime)
        }
    }
}

data class ColorBand(val name: String, val digit: Int, val color: Color, val multiplier: Double)

@Composable
private fun ResistorColorCodeCalculator() {
    val bands = remember {
        listOf(
            ColorBand("Black", 0, Color(0xFF1E1E1E), 1.0),
            ColorBand("Brown", 1, Color(0xFF8B4513), 10.0),
            ColorBand("Red", 2, Color(0xFFDC2626), 100.0),
            ColorBand("Orange", 3, Color(0xFFFF861C), 1000.0),
            ColorBand("Yellow", 4, Color(0xFFFACC15), 10000.0),
            ColorBand("Green", 5, Color(0xFF16A34A), 100000.0),
            ColorBand("Blue", 6, Color(0xFF2563EB), 1000000.0),
            ColorBand("Violet", 7, Color(0xFF7C3AED), 10000000.0),
            ColorBand("Grey", 8, Color(0xFF6B7280), 100000000.0),
            ColorBand("White", 9, Color(0xFFFFFFFF), 1000000000.0)
        )
    }

    var band1 by remember { mutableStateOf(bands[1]) } // Brown (1)
    var band2 by remember { mutableStateOf(bands[0]) } // Black (0)
    var band3Multiplier by remember { mutableStateOf(bands[2]) } // Red (100)

    val resistance = ((band1.digit * 10) + band2.digit) * band3Multiplier.multiplier
    val formatted = when {
        resistance >= 1e6 -> "${resistance / 1e6} MΩ"
        resistance >= 1e3 -> "${resistance / 1e3} kΩ"
        else -> "$resistance Ω"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Resistor 4-Band Color Code", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        // Resistor representation graphic
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFD2B48C))
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.width(14.dp).height(48.dp).background(band1.color))
            Box(modifier = Modifier.width(14.dp).height(48.dp).background(band2.color))
            Box(modifier = Modifier.width(14.dp).height(48.dp).background(band3Multiplier.color))
            Box(modifier = Modifier.width(14.dp).height(48.dp).background(Color(0xFFFFD700))) // Gold ±5%
        }

        // Selectors
        BandDropdown(label = "Band 1 (1st Digit)", selected = band1, bands = bands, onSelect = { band1 = it })
        BandDropdown(label = "Band 2 (2nd Digit)", selected = band2, bands = bands, onSelect = { band2 = it })
        BandDropdown(label = "Band 3 (Multiplier)", selected = band3Multiplier, bands = bands, onSelect = { band3Multiplier = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Resistance Value", value = formatted)
            ResultRow(title = "Tolerance", value = "± 5% (Gold)")
        }
    }
}

@Composable
private fun BandDropdown(
    label: String,
    selected: ColorBand,
    bands: List<ColorBand>,
    onSelect: (ColorBand) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, color = TextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(CharcoalButton)
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(selected.color)
                        .border(1.dp, CharcoalCardBorder, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("${selected.name} (${selected.digit})", color = PureWhite, fontWeight = FontWeight.Medium)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(CharcoalCard)
        ) {
            bands.forEach { b ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(b.color))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("${b.name} (${b.digit})", color = PureWhite)
                        }
                    },
                    onClick = {
                        onSelect(b)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ResistorNetworkCalculator(df: DecimalFormat) {
    var r1Str by remember { mutableStateOf("100") }
    var r2Str by remember { mutableStateOf("220") }
    var r3Str by remember { mutableStateOf("470") }

    val r1 = r1Str.toDoubleOrNull() ?: 0.0
    val r2 = r2Str.toDoubleOrNull() ?: 0.0
    val r3 = r3Str.toDoubleOrNull() ?: 0.0

    val series = r1 + r2 + r3
    val invParallel = (if (r1 > 0) 1.0 / r1 else 0.0) +
            (if (r2 > 0) 1.0 / r2 else 0.0) +
            (if (r3 > 0) 1.0 / r3 else 0.0)
    val parallel = if (invParallel > 0) 1.0 / invParallel else 0.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Resistors in Series & Parallel", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        EngineeringInput(label = "Resistor 1 (R₁) in Ω", value = r1Str, onValueChange = { r1Str = it })
        EngineeringInput(label = "Resistor 2 (R₂) in Ω", value = r2Str, onValueChange = { r2Str = it })
        EngineeringInput(label = "Resistor 3 (R₃) in Ω (optional)", value = r3Str, onValueChange = { r3Str = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Total Series Resistance", value = "${df.format(series)} Ω")
            ResultRow(title = "Total Parallel Resistance", value = "${df.format(parallel)} Ω")
        }
    }
}

@Composable
private fun TorqueHydraulicsCalculator(df: DecimalFormat) {
    var fStr by remember { mutableStateOf("250") } // Force in N
    var rStr by remember { mutableStateOf("0.4") } // Radius in m
    var pStr by remember { mutableStateOf("200000") } // Pressure in Pa
    var aStr by remember { mutableStateOf("0.015") } // Area in m^2

    val f = fStr.toDoubleOrNull() ?: 0.0
    val r = rStr.toDoubleOrNull() ?: 0.0
    val p = pStr.toDoubleOrNull() ?: 0.0
    val a = aStr.toDoubleOrNull() ?: 0.0

    val torque = f * r
    val hydraulicForce = p * a

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Torque (T = F·r) & Hydraulics (F = P·A)", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        EngineeringInput(label = "Applied Force (F) in Newtons", value = fStr, onValueChange = { fStr = it })
        EngineeringInput(label = "Lever Arm Radius (r) in meters", value = rStr, onValueChange = { rStr = it })
        EngineeringInput(label = "Hydraulic Pressure (P) in Pascals", value = pStr, onValueChange = { pStr = it })
        EngineeringInput(label = "Piston Area (A) in m²", value = aStr, onValueChange = { aStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Generated Torque (T)", value = "${df.format(torque)} N·m")
            ResultRow(title = "Hydraulic Force Output", value = "${df.format(hydraulicForce)} N")
        }
    }
}

@Composable
private fun EngineeringInput(
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
