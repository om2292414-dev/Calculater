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
import kotlin.math.acos
import kotlin.math.sqrt

@Composable
fun JeeCalculatorsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    val modules = listOf("Percentile & Rank", "Quadratic Solver", "Vectors (3D)", "2x2 / 3x3 Matrix")
    var selectedModule by remember { mutableStateOf("Percentile & Rank") }
    val df = remember { DecimalFormat("#,##0.####") }

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
                text = "JEE & Engineering Math",
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
                    "Percentile & Rank" -> JeeRankCalculator()
                    "Quadratic Solver" -> QuadraticRootsCalculator(df)
                    "Vectors (3D)" -> Vectors3DCalculator(df)
                    "2x2 / 3x3 Matrix" -> MatrixDeterminantCalculator(df)
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun JeeRankCalculator() {
    var marksStr by remember { mutableStateOf("185") }
    var totalCandidatesStr by remember { mutableStateOf("1200000") }

    val marks = marksStr.toIntOrNull() ?: 0
    val totalCandidates = totalCandidatesStr.toDoubleOrNull() ?: 1200000.0

    // Typical NTA score curve interpolation for 300 marks
    val percentile = when {
        marks >= 280 -> 99.98
        marks >= 250 -> 99.75
        marks >= 220 -> 99.30
        marks >= 190 -> 98.60
        marks >= 160 -> 97.20
        marks >= 130 -> 94.50
        marks >= 100 -> 89.00
        marks >= 70 -> 78.00
        marks >= 40 -> 60.00
        else -> 35.00
    }

    val estimatedRank = ((100.0 - percentile) / 100.0 * totalCandidates).toInt().coerceAtLeast(1)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("JEE Main Score to Percentile & Rank Predictor", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        JeeInput(label = "Expected Score / Marks (out of 300)", value = marksStr, onValueChange = { marksStr = it })
        JeeInput(label = "Total Registered Candidates", value = totalCandidatesStr, onValueChange = { totalCandidatesStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Estimated Percentile", value = "$percentile %ile")
            ResultRow(title = "Estimated AIR Rank", value = "AIR ~ $estimatedRank")
            ResultRow(
                title = "College Eligibility",
                value = if (percentile >= 98.0) "Top NITs / IIITs (CSE/ECE)" else if (percentile >= 92.0) "NITs Core / State Govt" else "Qualifying / State Engg"
            )
        }
    }
}

@Composable
private fun QuadraticRootsCalculator(df: DecimalFormat) {
    var aStr by remember { mutableStateOf("1") }
    var bStr by remember { mutableStateOf("-5") }
    var cStr by remember { mutableStateOf("6") }

    val a = aStr.toDoubleOrNull() ?: 1.0
    val b = bStr.toDoubleOrNull() ?: 0.0
    val c = cStr.toDoubleOrNull() ?: 0.0

    val discriminant = (b * b) - (4 * a * c)
    val rootInfo = when {
        discriminant > 0 -> {
            val r1 = (-b + sqrt(discriminant)) / (2 * a)
            val r2 = (-b - sqrt(discriminant)) / (2 * a)
            "Two Real Roots: x₁ = ${df.format(r1)}, x₂ = ${df.format(r2)}"
        }
        discriminant == 0.0 -> {
            val r = -b / (2 * a)
            "One Repeated Root: x = ${df.format(r)}"
        }
        else -> {
            val real = -b / (2 * a)
            val imag = sqrt(-discriminant) / (2 * a)
            "Complex Roots: ${df.format(real)} ± ${df.format(imag)}i"
        }
    }

    val vertexX = -b / (2 * a)
    val vertexY = (a * vertexX * vertexX) + (b * vertexX) + c

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Quadratic Equation Solver (ax² + bx + c = 0)", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            JeeInput(label = "a", value = aStr, modifier = Modifier.weight(1f), onValueChange = { aStr = it })
            JeeInput(label = "b", value = bStr, modifier = Modifier.weight(1f), onValueChange = { bStr = it })
            JeeInput(label = "c", value = cStr, modifier = Modifier.weight(1f), onValueChange = { cStr = it })
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Discriminant (Δ = b² - 4ac)", value = df.format(discriminant))
            ResultRow(title = "Roots", value = rootInfo)
            ResultRow(title = "Parabola Vertex (h, k)", value = "(${df.format(vertexX)}, ${df.format(vertexY)})")
        }
    }
}

@Composable
private fun Vectors3DCalculator(df: DecimalFormat) {
    var axStr by remember { mutableStateOf("2") }
    var ayStr by remember { mutableStateOf("3") }
    var azStr by remember { mutableStateOf("1") }

    var bxStr by remember { mutableStateOf("1") }
    var byStr by remember { mutableStateOf("-2") }
    var bzStr by remember { mutableStateOf("4") }

    val ax = axStr.toDoubleOrNull() ?: 0.0
    val ay = ayStr.toDoubleOrNull() ?: 0.0
    val az = azStr.toDoubleOrNull() ?: 0.0

    val bx = bxStr.toDoubleOrNull() ?: 0.0
    val by = byStr.toDoubleOrNull() ?: 0.0
    val bz = bzStr.toDoubleOrNull() ?: 0.0

    val dotProduct = (ax * bx) + (ay * by) + (az * bz)
    val magA = sqrt((ax * ax) + (ay * ay) + (az * az))
    val magB = sqrt((bx * bx) + (by * by) + (bz * bz))

    val crossX = (ay * bz) - (az * by)
    val crossY = (az * bx) - (ax * bz)
    val crossZ = (ax * by) - (ay * bx)

    val angleRad = if (magA * magB > 0) acos((dotProduct / (magA * magB)).coerceIn(-1.0, 1.0)) else 0.0
    val angleDeg = Math.toDegrees(angleRad)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("3D Vectors: A = axî + ayĵ + azk̂", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Text("Vector A Components", color = OrangeEquals, fontSize = 13.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            JeeInput(label = "Ax", value = axStr, modifier = Modifier.weight(1f), onValueChange = { axStr = it })
            JeeInput(label = "Ay", value = ayStr, modifier = Modifier.weight(1f), onValueChange = { ayStr = it })
            JeeInput(label = "Az", value = azStr, modifier = Modifier.weight(1f), onValueChange = { azStr = it })
        }

        Text("Vector B Components", color = OrangeEquals, fontSize = 13.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            JeeInput(label = "Bx", value = bxStr, modifier = Modifier.weight(1f), onValueChange = { bxStr = it })
            JeeInput(label = "By", value = byStr, modifier = Modifier.weight(1f), onValueChange = { byStr = it })
            JeeInput(label = "Bz", value = bzStr, modifier = Modifier.weight(1f), onValueChange = { bzStr = it })
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Dot Product (A · B)", value = df.format(dotProduct))
            ResultRow(title = "Cross Product (A × B)", value = "${df.format(crossX)}î + ${df.format(crossY)}ĵ + ${df.format(crossZ)}k̂")
            ResultRow(title = "|A| Magnitude", value = df.format(magA))
            ResultRow(title = "|B| Magnitude", value = df.format(magB))
            ResultRow(title = "Angle Between (θ)", value = "${df.format(angleDeg)}°")
        }
    }
}

@Composable
private fun MatrixDeterminantCalculator(df: DecimalFormat) {
    var a11 by remember { mutableStateOf("1") }
    var a12 by remember { mutableStateOf("2") }
    var a13 by remember { mutableStateOf("3") }
    var a21 by remember { mutableStateOf("0") }
    var a22 by remember { mutableStateOf("1") }
    var a23 by remember { mutableStateOf("4") }
    var a31 by remember { mutableStateOf("5") }
    var a32 by remember { mutableStateOf("6") }
    var a33 by remember { mutableStateOf("0") }

    val m11 = a11.toDoubleOrNull() ?: 0.0
    val m12 = a12.toDoubleOrNull() ?: 0.0
    val m13 = a13.toDoubleOrNull() ?: 0.0
    val m21 = a21.toDoubleOrNull() ?: 0.0
    val m22 = a22.toDoubleOrNull() ?: 0.0
    val m23 = a23.toDoubleOrNull() ?: 0.0
    val m31 = a31.toDoubleOrNull() ?: 0.0
    val m32 = a32.toDoubleOrNull() ?: 0.0
    val m33 = a33.toDoubleOrNull() ?: 0.0

    val det3x3 = m11 * (m22 * m33 - m23 * m32) -
            m12 * (m21 * m33 - m23 * m31) +
            m13 * (m21 * m32 - m22 * m31)

    val det2x2 = (m11 * m22) - (m12 * m21)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("3×3 Matrix Determinant & Properties", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            JeeInput(label = "a11", value = a11, modifier = Modifier.weight(1f), onValueChange = { a11 = it })
            JeeInput(label = "a12", value = a12, modifier = Modifier.weight(1f), onValueChange = { a12 = it })
            JeeInput(label = "a13", value = a13, modifier = Modifier.weight(1f), onValueChange = { a13 = it })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            JeeInput(label = "a21", value = a21, modifier = Modifier.weight(1f), onValueChange = { a21 = it })
            JeeInput(label = "a22", value = a22, modifier = Modifier.weight(1f), onValueChange = { a22 = it })
            JeeInput(label = "a23", value = a23, modifier = Modifier.weight(1f), onValueChange = { a23 = it })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            JeeInput(label = "a31", value = a31, modifier = Modifier.weight(1f), onValueChange = { a31 = it })
            JeeInput(label = "a32", value = a32, modifier = Modifier.weight(1f), onValueChange = { a32 = it })
            JeeInput(label = "a33", value = a33, modifier = Modifier.weight(1f), onValueChange = { a33 = it })
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Determinant |A| (3×3)", value = df.format(det3x3))
            ResultRow(title = "Top-Left 2×2 Sub-Determinant", value = df.format(det2x2))
            ResultRow(title = "Invertible?", value = if (det3x3 != 0.0) "Yes (Non-Singular)" else "No (Singular, Det=0)")
        }
    }
}

@Composable
private fun JeeInput(
    label: String,
    value: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = TextSecondary, fontSize = 11.sp) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CharcoalButton,
            unfocusedContainerColor = CharcoalButton,
            focusedBorderColor = OrangeEquals,
            unfocusedBorderColor = CharcoalCardBorder,
            focusedTextColor = PureWhite,
            unfocusedTextColor = PureWhite
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    )
}
