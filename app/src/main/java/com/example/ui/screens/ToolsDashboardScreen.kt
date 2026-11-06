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
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.ViewInAr
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
import androidx.compose.ui.graphics.vector.ImageVector
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

enum class ToolDestination {
    MAIN_CALC,
    TOOLS_DASHBOARD,
    CURRENCY,
    UNIT_CONVERTER,
    PHYSICS,
    ENGINEERING,
    JEE,
    FINANCE
}

data class ToolItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val icon: ImageVector,
    val destination: ToolDestination,
    val subType: String = ""
)

@Composable
fun ToolsDashboardScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTool: (ToolDestination, String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Converters", "Physics", "Engineering", "JEE", "Finance")

    val tools = remember {
        listOf(
            ToolItem(
                id = "currency",
                title = "Live Currency Converter",
                description = "Real-time rates across 30+ world currencies (USD, EUR, INR, GBP, JPY...)",
                category = "Converters",
                icon = Icons.Outlined.AttachMoney,
                destination = ToolDestination.CURRENCY
            ),
            ToolItem(
                id = "unit_pressure",
                title = "Pressure Calculator",
                description = "Pa, bar, psi, atm, mmHg, kPa, torr conversions",
                category = "Converters",
                icon = Icons.Outlined.Speed,
                destination = ToolDestination.UNIT_CONVERTER,
                subType = "Pressure"
            ),
            ToolItem(
                id = "unit_power",
                title = "Power Calculator",
                description = "Watts, kW, Horsepower (hp), BTU/h, ft-lb/s",
                category = "Converters",
                icon = Icons.Outlined.ElectricBolt,
                destination = ToolDestination.UNIT_CONVERTER,
                subType = "Power"
            ),
            ToolItem(
                id = "unit_volume",
                title = "Volume Calculator",
                description = "Liters, mL, m³, Gallons, Quarts, Cups, fl oz",
                category = "Converters",
                icon = Icons.Outlined.ViewInAr,
                destination = ToolDestination.UNIT_CONVERTER,
                subType = "Volume"
            ),
            ToolItem(
                id = "unit_area",
                title = "Area Calculator",
                description = "m², km², Square Feet, Yard, Acre, Hectare",
                category = "Converters",
                icon = Icons.Outlined.Straighten,
                destination = ToolDestination.UNIT_CONVERTER,
                subType = "Area"
            ),
            ToolItem(
                id = "unit_temp",
                title = "Temperature Converter",
                description = "Celsius (°C), Fahrenheit (°F), Kelvin (K)",
                category = "Converters",
                icon = Icons.Outlined.Thermostat,
                destination = ToolDestination.UNIT_CONVERTER,
                subType = "Temperature"
            ),
            ToolItem(
                id = "unit_weight",
                title = "Weight & Mass Converter",
                description = "Kilograms, Grams, Pounds (lb), Ounces (oz), Metric Ton",
                category = "Converters",
                icon = Icons.Outlined.Scale,
                destination = ToolDestination.UNIT_CONVERTER,
                subType = "Weight"
            ),
            ToolItem(
                id = "unit_length",
                title = "Length & Distance",
                description = "Meters, Kilometers, Centimeters, Inches, Feet, Miles",
                category = "Converters",
                icon = Icons.Outlined.Straighten,
                destination = ToolDestination.UNIT_CONVERTER,
                subType = "Length"
            ),
            ToolItem(
                id = "unit_speed",
                title = "Speed Converter",
                description = "km/h, mph, m/s, Knots, ft/s",
                category = "Converters",
                icon = Icons.Outlined.Speed,
                destination = ToolDestination.UNIT_CONVERTER,
                subType = "Speed"
            ),
            // Physics
            ToolItem(
                id = "physics_suite",
                title = "Physics Suite",
                description = "Velocity (v=u+at), Force (F=ma), Kinetic/Potential Energy, Ohm's Law, Gravity",
                category = "Physics",
                icon = Icons.Outlined.Functions,
                destination = ToolDestination.PHYSICS
            ),
            // Engineering
            ToolItem(
                id = "engineering_suite",
                title = "Engineering Suite",
                description = "Stress & Strain, Reynolds Number (Re), Resistor 4/5 Band & Networks, Torque",
                category = "Engineering",
                icon = Icons.Outlined.PrecisionManufacturing,
                destination = ToolDestination.ENGINEERING
            ),
            // JEE
            ToolItem(
                id = "jee_suite",
                title = "JEE Calculator",
                description = "Main/Adv Percentile & Rank Predictor, Polynomial Roots Solver, 2D/3D Vectors & Matrices",
                category = "JEE",
                icon = Icons.Outlined.School,
                destination = ToolDestination.JEE
            ),
            // Finance
            ToolItem(
                id = "finance_suite",
                title = "Financial Calculators",
                description = "Compound Interest, Loan/EMI, GST/Sales Tax, Tip & Split, Discount, SIP Wealth",
                category = "Finance",
                icon = Icons.Outlined.AccountBalance,
                destination = ToolDestination.FINANCE
            )
        )
    }

    val filteredTools = remember(searchQuery, selectedCategory, tools) {
        tools.filter { item ->
            val matchesCategory = selectedCategory == "All" || item.category == selectedCategory
            val matchesQuery = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.description.contains(searchQuery, ignoreCase = true) ||
                    item.category.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.testTag("tools_back_btn").size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back to Calculator",
                    tint = PureWhite
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = "Calculator Tools",
                    color = PureWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Converters, Physics, Engineering, JEE & Finance",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search tools and calculators...", color = TextTertiary, fontSize = 14.sp) },
            leadingIcon = {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CharcoalCard,
                unfocusedContainerColor = CharcoalCard,
                focusedBorderColor = OrangeEquals,
                unfocusedBorderColor = CharcoalCardBorder,
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .testTag("tools_search_input")
        )

        // Category tabs
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSelected = cat == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = {
                        Text(
                            text = cat,
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

        // Tools List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredTools, key = { it.id }) { tool ->
                ToolCard(
                    tool = tool,
                    onClick = { onNavigateToTool(tool.destination, tool.subType) }
                )
            }
        }
    }
}

@Composable
private fun ToolCard(
    tool: ToolItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag("tool_card_${tool.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(CharcoalButton),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = tool.icon,
                contentDescription = null,
                tint = OrangeEquals,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tool.title,
                color = PureWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = tool.description,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
            contentDescription = null,
            tint = TextTertiary,
            modifier = Modifier.size(14.dp)
        )
    }
}
