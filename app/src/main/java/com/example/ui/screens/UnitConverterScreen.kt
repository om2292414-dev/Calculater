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
import androidx.compose.material.icons.outlined.SwapVert
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

data class UnitDef(val name: String, val factorToBase: Double)

@Composable
fun UnitConverterScreen(
    initialType: String = "Length",
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    val unitTypes = listOf("Length", "Weight", "Temperature", "Volume", "Area", "Speed", "Pressure", "Power")
    var selectedType by remember { mutableStateOf(if (initialType.isNotBlank() && initialType in unitTypes) initialType else "Length") }

    val unitDefinitions = remember(selectedType) {
        when (selectedType) {
            "Length" -> listOf(
                UnitDef("Meters (m)", 1.0),
                UnitDef("Kilometers (km)", 1000.0),
                UnitDef("Centimeters (cm)", 0.01),
                UnitDef("Millimeters (mm)", 0.001),
                UnitDef("Inches (in)", 0.0254),
                UnitDef("Feet (ft)", 0.3048),
                UnitDef("Yards (yd)", 0.9144),
                UnitDef("Miles (mi)", 1609.344),
                UnitDef("Nautical Miles (NM)", 1852.0)
            )
            "Weight" -> listOf(
                UnitDef("Kilograms (kg)", 1.0),
                UnitDef("Grams (g)", 0.001),
                UnitDef("Milligrams (mg)", 0.000001),
                UnitDef("Pounds (lb)", 0.45359237),
                UnitDef("Ounces (oz)", 0.0283495),
                UnitDef("Metric Tons (t)", 1000.0)
            )
            "Temperature" -> listOf(
                UnitDef("Celsius (°C)", 1.0),
                UnitDef("Fahrenheit (°F)", 1.0),
                UnitDef("Kelvin (K)", 1.0)
            )
            "Volume" -> listOf(
                UnitDef("Liters (L)", 1.0),
                UnitDef("Milliliters (mL)", 0.001),
                UnitDef("Cubic Meters (m³)", 1000.0),
                UnitDef("US Gallons (gal)", 3.78541),
                UnitDef("US Quarts (qt)", 0.946353),
                UnitDef("US Cups", 0.236588),
                UnitDef("Fluid Ounces (fl oz)", 0.0295735)
            )
            "Area" -> listOf(
                UnitDef("Square Meters (m²)", 1.0),
                UnitDef("Square Kilometers (km²)", 1000000.0),
                UnitDef("Square Feet (ft²)", 0.092903),
                UnitDef("Square Yards (yd²)", 0.836127),
                UnitDef("Acres (ac)", 4046.86),
                UnitDef("Hectares (ha)", 10000.0)
            )
            "Speed" -> listOf(
                UnitDef("Meters/sec (m/s)", 1.0),
                UnitDef("Kilometers/hour (km/h)", 0.277778),
                UnitDef("Miles/hour (mph)", 0.44704),
                UnitDef("Knots (kn)", 0.514444),
                UnitDef("Feet/sec (ft/s)", 0.3048)
            )
            "Pressure" -> listOf(
                UnitDef("Pascals (Pa)", 1.0),
                UnitDef("Kilopascals (kPa)", 1000.0),
                UnitDef("Bar (bar)", 100000.0),
                UnitDef("PSI (lbf/in²)", 6894.76),
                UnitDef("Atmospheres (atm)", 101325.0),
                UnitDef("Millimeters of Mercury (mmHg)", 133.322),
                UnitDef("Torr", 133.322)
            )
            "Power" -> listOf(
                UnitDef("Watts (W)", 1.0),
                UnitDef("Kilowatts (kW)", 1000.0),
                UnitDef("Megawatts (MW)", 1000000.0),
                UnitDef("Horsepower (hp)", 745.7),
                UnitDef("BTU/hour", 0.293071),
                UnitDef("Foot-pounds/sec", 1.35582)
            )
            else -> listOf(UnitDef("Standard", 1.0))
        }
    }

    var fromUnit by remember(selectedType) { mutableStateOf(unitDefinitions[0]) }
    var toUnit by remember(selectedType) { mutableStateOf(if (unitDefinitions.size > 1) unitDefinitions[1] else unitDefinitions[0]) }
    var inputStr by remember { mutableStateOf("1") }
    var fromMenuOpen by remember { mutableStateOf(false) }
    var toMenuOpen by remember { mutableStateOf(false) }

    val inputVal = inputStr.toDoubleOrNull() ?: 0.0

    val convertedVal = remember(selectedType, fromUnit, toUnit, inputVal) {
        if (selectedType == "Temperature") {
            // Temperature special conversion
            val from = fromUnit.name
            val to = toUnit.name
            val celsius = when {
                from.contains("Celsius") -> inputVal
                from.contains("Fahrenheit") -> (inputVal - 32) * 5.0 / 9.0
                from.contains("Kelvin") -> inputVal - 273.15
                else -> inputVal
            }
            when {
                to.contains("Celsius") -> celsius
                to.contains("Fahrenheit") -> (celsius * 9.0 / 5.0) + 32
                to.contains("Kelvin") -> celsius + 273.15
                else -> celsius
            }
        } else {
            val baseVal = inputVal * fromUnit.factorToBase
            baseVal / toUnit.factorToBase
        }
    }

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
                text = "$selectedType Converter",
                color = PureWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Horizontal Unit Types
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(unitTypes) { type ->
                val isSelected = type == selectedType
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedType = type },
                    label = {
                        Text(
                            text = type,
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
                // Interactive Conversion Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CharcoalCard)
                        .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    // From Unit & Input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CharcoalButton)
                                    .clickable { fromMenuOpen = true }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = fromUnit.name,
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            DropdownMenu(
                                expanded = fromMenuOpen,
                                onDismissRequest = { fromMenuOpen = false },
                                modifier = Modifier.background(CharcoalCard)
                            ) {
                                unitDefinitions.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text(unit.name, color = PureWhite) },
                                        onClick = {
                                            fromUnit = unit
                                            fromMenuOpen = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = inputStr,
                            onValueChange = { inputStr = it },
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = PureWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CharcoalButton,
                                unfocusedContainerColor = CharcoalButton,
                                focusedBorderColor = OrangeEquals,
                                unfocusedBorderColor = CharcoalCardBorder
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .width(150.dp)
                                .testTag("unit_input_val")
                        )
                    }

                    // Swap Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(OrangeEquals)
                                .clickable {
                                    val temp = fromUnit
                                    fromUnit = toUnit
                                    toUnit = temp
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SwapVert,
                                contentDescription = "Swap Units",
                                tint = PureWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // To Unit & Output
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CharcoalButton)
                                    .clickable { toMenuOpen = true }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = toUnit.name,
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            DropdownMenu(
                                expanded = toMenuOpen,
                                onDismissRequest = { toMenuOpen = false },
                                modifier = Modifier.background(CharcoalCard)
                            ) {
                                unitDefinitions.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text(unit.name, color = PureWhite) },
                                        onClick = {
                                            toUnit = unit
                                            toMenuOpen = false
                                        }
                                    )
                                }
                            }
                        }

                        Text(
                            text = df.format(convertedVal),
                            color = OrangeEquals,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            item {
                Text(
                    text = "All Available Units Breakdown",
                    color = PureWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CharcoalCard)
                        .border(1.dp, CharcoalCardBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    unitDefinitions.forEach { u ->
                        val targetVal = if (selectedType == "Temperature") {
                            val c = when {
                                fromUnit.name.contains("Celsius") -> inputVal
                                fromUnit.name.contains("Fahrenheit") -> (inputVal - 32) * 5.0 / 9.0
                                fromUnit.name.contains("Kelvin") -> inputVal - 273.15
                                else -> inputVal
                            }
                            when {
                                u.name.contains("Celsius") -> c
                                u.name.contains("Fahrenheit") -> (c * 9.0 / 5.0) + 32
                                u.name.contains("Kelvin") -> c + 273.15
                                else -> c
                            }
                        } else {
                            (inputVal * fromUnit.factorToBase) / u.factorToBase
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = u.name, color = TextSecondary, fontSize = 14.sp)
                            Text(
                                text = df.format(targetVal),
                                color = PureWhite,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
