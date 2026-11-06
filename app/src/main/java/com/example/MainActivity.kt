package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.model.HistoryItem
import com.example.ui.screens.CurrencyConverterScreen
import com.example.ui.screens.EngineeringCalculatorsScreen
import com.example.ui.screens.FinancialCalculatorsScreen
import com.example.ui.screens.JeeCalculatorsScreen
import com.example.ui.screens.MainCalculatorScreen
import com.example.ui.screens.PhysicsCalculatorsScreen
import com.example.ui.screens.ToolDestination
import com.example.ui.screens.ToolsDashboardScreen
import com.example.ui.screens.UnitConverterScreen
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.PureWhite
import com.example.ui.theme.UltraCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UltraCalculatorTheme {
                UltraCalculatorApp()
            }
        }
    }
}

@Composable
fun UltraCalculatorApp() {
    var currentScreen by remember { mutableStateOf(ToolDestination.MAIN_CALC) }
    var activeSubType by remember { mutableStateOf("") }
    val historyList = remember { mutableStateListOf<HistoryItem>() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AmoledBlack,
        contentColor = PureWhite,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AmoledBlack)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    ToolDestination.MAIN_CALC -> {
                        MainCalculatorScreen(
                            historyList = historyList,
                            onOpenToolsDashboard = {
                                currentScreen = ToolDestination.TOOLS_DASHBOARD
                            }
                        )
                    }
                    ToolDestination.TOOLS_DASHBOARD -> {
                        ToolsDashboardScreen(
                            onNavigateBack = { currentScreen = ToolDestination.MAIN_CALC },
                            onNavigateToTool = { dest, subType ->
                                activeSubType = subType
                                currentScreen = dest
                            }
                        )
                    }
                    ToolDestination.CURRENCY -> {
                        CurrencyConverterScreen(
                            onNavigateBack = { currentScreen = ToolDestination.TOOLS_DASHBOARD }
                        )
                    }
                    ToolDestination.UNIT_CONVERTER -> {
                        UnitConverterScreen(
                            initialType = activeSubType,
                            onNavigateBack = { currentScreen = ToolDestination.TOOLS_DASHBOARD }
                        )
                    }
                    ToolDestination.PHYSICS -> {
                        PhysicsCalculatorsScreen(
                            onNavigateBack = { currentScreen = ToolDestination.TOOLS_DASHBOARD }
                        )
                    }
                    ToolDestination.ENGINEERING -> {
                        EngineeringCalculatorsScreen(
                            onNavigateBack = { currentScreen = ToolDestination.TOOLS_DASHBOARD }
                        )
                    }
                    ToolDestination.JEE -> {
                        JeeCalculatorsScreen(
                            onNavigateBack = { currentScreen = ToolDestination.TOOLS_DASHBOARD }
                        )
                    }
                    ToolDestination.FINANCE -> {
                        FinancialCalculatorsScreen(
                            onNavigateBack = { currentScreen = ToolDestination.TOOLS_DASHBOARD }
                        )
                    }
                }
            }
        }
    }
}
