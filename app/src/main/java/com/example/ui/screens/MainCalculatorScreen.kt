package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AngleUnit
import com.example.model.CalculatorEngine
import com.example.model.HistoryItem
import com.example.ui.components.CalculatorButton
import com.example.ui.components.TopToolbar
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.OrangeEquals
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun MainCalculatorScreen(
    historyList: MutableList<HistoryItem>,
    onOpenToolsDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expression by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("0") }
    var lastEvaluatedExpr by remember { mutableStateOf("") }
    var isScientificExpanded by remember { mutableStateOf(false) }
    var isHistorySheetOpen by remember { mutableStateOf(false) }
    var angleUnit by remember { mutableStateOf(AngleUnit.DEG) }
    var isResultEvaluated by remember { mutableStateOf(false) }

    val exprScrollState = rememberScrollState()

    // Auto-scroll expression to the end whenever it changes
    LaunchedEffect(expression) {
        exprScrollState.animateScrollTo(exprScrollState.maxValue)
    }

    // Dynamic live preview evaluation
    LaunchedEffect(expression, angleUnit) {
        if (expression.isNotBlank() && !isResultEvaluated) {
            val eval = CalculatorEngine.evaluate(expression, angleUnit)
            if (eval.isSuccess) {
                resultText = CalculatorEngine.formatNumber(eval.getOrThrow())
            }
        } else if (expression.isBlank() && !isResultEvaluated) {
            resultText = "0"
        }
    }

    fun onAppendToken(token: String) {
        if (isResultEvaluated) {
            // If user just got a result and types an operator, continue with result
            if (token in listOf("+", "−", "×", "÷", "%", "^")) {
                expression = resultText + token
            } else {
                expression = token
            }
            isResultEvaluated = false
        } else {
            // Prevent invalid duplicate consecutive operators like ++ or ×÷
            val operators = listOf("+", "−", "×", "÷", "%")
            if (token in operators && expression.isNotEmpty()) {
                val lastChar = expression.last().toString()
                if (lastChar in operators) {
                    expression = expression.dropLast(1) + token
                    return
                }
            }
            expression += token
        }
    }

    fun onBackspace() {
        if (isResultEvaluated) {
            expression = ""
            resultText = "0"
            isResultEvaluated = false
            return
        }
        if (expression.isNotEmpty()) {
            // Handle scientific multi-char tokens like "sin(", "asin(", "log(", "ln(", "√("
            val functionTokens = listOf("asin(", "acos(", "atan(", "sin(", "cos(", "tan(", "log(", "ln(", "√(")
            var removed = false
            for (fn in functionTokens) {
                if (expression.endsWith(fn)) {
                    expression = expression.dropLast(fn.length)
                    removed = true
                    break
                }
            }
            if (!removed) {
                expression = expression.dropLast(1)
            }
        }
    }

    fun onClearAll() {
        expression = ""
        resultText = "0"
        lastEvaluatedExpr = ""
        isResultEvaluated = false
    }

    fun onCalculateEquals() {
        if (expression.isBlank()) return
        val eval = CalculatorEngine.evaluate(expression, angleUnit)
        if (eval.isSuccess) {
            val formatted = CalculatorEngine.formatNumber(eval.getOrThrow())
            val historyItem = HistoryItem(
                expression = expression,
                result = formatted,
                timestamp = System.currentTimeMillis()
            )
            // Add to chronological history (at start so recent are accessible)
            historyList.add(0, historyItem)
            lastEvaluatedExpr = expression
            resultText = formatted
            isResultEvaluated = true
        } else {
            val err = eval.exceptionOrNull()?.message ?: "Error"
            resultText = if (err.contains("zero", ignoreCase = true)) "Cannot divide by 0" else "Invalid Format"
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .align(Alignment.Center)
        ) {
            // Top Toolbar: 4 minimalist white icons
            TopToolbar(
                isScientificExpanded = isScientificExpanded,
                angleUnit = angleUnit,
                onToggleFullscreen = { isHistorySheetOpen = true },
                onToggleScientific = { isScientificExpanded = !isScientificExpanded },
                onOpenToolsDashboard = onOpenToolsDashboard,
                onOpenHistory = { isHistorySheetOpen = true },
                onToggleAngleUnit = {
                    angleUnit = if (angleUnit == AngleUnit.DEG) AngleUnit.RAD else AngleUnit.DEG
                },
                onClearAll = { onClearAll() }
            )

            // Upper Display Section: large empty calculation display area with expression and result
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    // Swipe down gesture to open History
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            if (dragAmount > 20) {
                                isHistorySheetOpen = true
                            }
                        }
                    },
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    // Previous calculation expression (faintly displayed when evaluating)
                    if (isResultEvaluated && lastEvaluatedExpr.isNotEmpty()) {
                        Text(
                            text = lastEvaluatedExpr,
                            color = TextTertiary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // Current expression line (aligned to the right, scrollable horizontally if long)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(exprScrollState),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = if (isResultEvaluated) "" else expression,
                            color = TextSecondary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.End,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Large Result display in pure white text
                    val resultFontSize = when {
                        resultText.length > 15 -> 32.sp
                        resultText.length > 10 -> 40.sp
                        resultText.length > 7 -> 48.sp
                        else -> 56.sp
                    }

                    Text(
                        text = resultText,
                        color = PureWhite,
                        fontSize = resultFontSize,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("calc_display_result")
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Scientific panel (if toggled open)
            ScientificPanel(
                isExpanded = isScientificExpanded,
                angleUnit = angleUnit,
                onToggleAngleUnit = {
                    angleUnit = if (angleUnit == AngleUnit.DEG) AngleUnit.RAD else AngleUnit.DEG
                },
                onInsert = { token -> onAppendToken(token) }
            )

            // Keypad Section: 5 rows of circular buttons, 4 buttons per row
            // Dark charcoal #292929, white numbers/symbols, orange equals #FF861C
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Row 1: AC | % | Backspace | ÷
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "AC",
                        modifier = Modifier.weight(1f),
                        fontSize = 24.sp,
                        testTag = "calc_btn_ac",
                        onClick = { onClearAll() }
                    )
                    CalculatorButton(
                        text = "%",
                        modifier = Modifier.weight(1f),
                        fontSize = 26.sp,
                        testTag = "calc_btn_percent",
                        onClick = { onAppendToken("%") }
                    )
                    CalculatorButton(
                        text = "⌫",
                        modifier = Modifier.weight(1f),
                        fontSize = 24.sp,
                        testTag = "calc_btn_backspace",
                        onLongClick = { onClearAll() },
                        onClick = { onBackspace() }
                    )
                    CalculatorButton(
                        text = "÷",
                        modifier = Modifier.weight(1f),
                        fontSize = 32.sp,
                        testTag = "calc_btn_div",
                        onClick = { onAppendToken("÷") }
                    )
                }

                // Row 2: 7 | 8 | 9 | ×
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "7",
                        modifier = Modifier.weight(1f),
                        testTag = "calc_btn_7",
                        onClick = { onAppendToken("7") }
                    )
                    CalculatorButton(
                        text = "8",
                        modifier = Modifier.weight(1f),
                        testTag = "calc_btn_8",
                        onClick = { onAppendToken("8") }
                    )
                    CalculatorButton(
                        text = "9",
                        modifier = Modifier.weight(1f),
                        testTag = "calc_btn_9",
                        onClick = { onAppendToken("9") }
                    )
                    CalculatorButton(
                        text = "×",
                        modifier = Modifier.weight(1f),
                        fontSize = 30.sp,
                        testTag = "calc_btn_mul",
                        onClick = { onAppendToken("×") }
                    )
                }

                // Row 3: 4 | 5 | 6 | −
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "4",
                        modifier = Modifier.weight(1f),
                        testTag = "calc_btn_4",
                        onClick = { onAppendToken("4") }
                    )
                    CalculatorButton(
                        text = "5",
                        modifier = Modifier.weight(1f),
                        testTag = "calc_btn_5",
                        onClick = { onAppendToken("5") }
                    )
                    CalculatorButton(
                        text = "6",
                        modifier = Modifier.weight(1f),
                        testTag = "calc_btn_6",
                        onClick = { onAppendToken("6") }
                    )
                    CalculatorButton(
                        text = "−",
                        modifier = Modifier.weight(1f),
                        fontSize = 32.sp,
                        testTag = "calc_btn_sub",
                        onClick = { onAppendToken("−") }
                    )
                }

                // Row 4: 1 | 2 | 3 | +
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "1",
                        modifier = Modifier.weight(1f),
                        testTag = "calc_btn_1",
                        onClick = { onAppendToken("1") }
                    )
                    CalculatorButton(
                        text = "2",
                        modifier = Modifier.weight(1f),
                        testTag = "calc_btn_2",
                        onClick = { onAppendToken("2") }
                    )
                    CalculatorButton(
                        text = "3",
                        modifier = Modifier.weight(1f),
                        testTag = "calc_btn_3",
                        onClick = { onAppendToken("3") }
                    )
                    CalculatorButton(
                        text = "+",
                        modifier = Modifier.weight(1f),
                        fontSize = 30.sp,
                        testTag = "calc_btn_add",
                        onClick = { onAppendToken("+") }
                    )
                }

                // Row 5: 00 | 0 | . | =
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "00",
                        modifier = Modifier.weight(1f),
                        fontSize = 24.sp,
                        testTag = "calc_btn_00",
                        onClick = { onAppendToken("00") }
                    )
                    CalculatorButton(
                        text = "0",
                        modifier = Modifier.weight(1f),
                        testTag = "calc_btn_0",
                        onClick = { onAppendToken("0") }
                    )
                    CalculatorButton(
                        text = ".",
                        modifier = Modifier.weight(1f),
                        fontSize = 32.sp,
                        testTag = "calc_btn_dot",
                        onClick = { onAppendToken(".") }
                    )
                    // The equals button MUST be orange (#FF861C)
                    CalculatorButton(
                        text = "=",
                        modifier = Modifier.weight(1f),
                        isEquals = true,
                        fontSize = 32.sp,
                        testTag = "calc_btn_equals",
                        onClick = { onCalculateEquals() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Calculation History Bottom Sheet / Screen
        if (isHistorySheetOpen) {
            HistorySheet(
                historyList = historyList,
                onSelectHistory = { item ->
                    expression = item.expression
                    resultText = item.result
                    isResultEvaluated = true
                    isHistorySheetOpen = false
                },
                onClearHistory = {
                    historyList.clear()
                },
                onDismiss = { isHistorySheetOpen = false }
            )
        }
    }
}
