package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.AngleUnit
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CharcoalButton
import com.example.ui.theme.CharcoalCard
import com.example.ui.theme.OrangeEquals
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextSecondary

@Composable
fun ScientificPanel(
    isExpanded: Boolean,
    angleUnit: AngleUnit,
    onToggleAngleUnit: () -> Unit,
    onInsert: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isInverse by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AmoledBlack)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: RAD/DEG | 2nd | sin / asin | cos / acos | tan / atan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScientificPillButton(
                    text = if (angleUnit == AngleUnit.DEG) "DEG" else "RAD",
                    isHighlight = true,
                    modifier = Modifier.weight(1f),
                    onClick = onToggleAngleUnit
                )
                ScientificPillButton(
                    text = "2nd",
                    isHighlight = isInverse,
                    modifier = Modifier.weight(1f),
                    onClick = { isInverse = !isInverse }
                )
                ScientificPillButton(
                    text = if (isInverse) "sin⁻¹" else "sin",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert(if (isInverse) "asin(" else "sin(") }
                )
                ScientificPillButton(
                    text = if (isInverse) "cos⁻¹" else "cos",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert(if (isInverse) "acos(" else "cos(") }
                )
                ScientificPillButton(
                    text = if (isInverse) "tan⁻¹" else "tan",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert(if (isInverse) "atan(" else "tan(") }
                )
            }

            // Row 2: ln | log | √ | x^y | !
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScientificPillButton(
                    text = if (isInverse) "eˣ" else "ln",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert(if (isInverse) "e^(" else "ln(") }
                )
                ScientificPillButton(
                    text = if (isInverse) "10ˣ" else "log",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert(if (isInverse) "10^(" else "log(") }
                )
                ScientificPillButton(
                    text = if (isInverse) "x²" else "√",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert(if (isInverse) "^2" else "√(") }
                )
                ScientificPillButton(
                    text = "^",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert("^") }
                )
                ScientificPillButton(
                    text = "x!",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert("!") }
                )
            }

            // Row 3: π | e | ( | ) | 1/x
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScientificPillButton(
                    text = "π",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert("π") }
                )
                ScientificPillButton(
                    text = "e",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert("e") }
                )
                ScientificPillButton(
                    text = "(",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert("(") }
                )
                ScientificPillButton(
                    text = ")",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert(")") }
                )
                ScientificPillButton(
                    text = "1/x",
                    modifier = Modifier.weight(1f),
                    onClick = { onInsert("1/(") }
                )
            }
        }
    }
}

@Composable
private fun ScientificPillButton(
    text: String,
    modifier: Modifier = Modifier,
    isHighlight: Boolean = false,
    onClick: () -> Unit
) {
    val bg = if (isHighlight) OrangeEquals else CharcoalCard
    val textColor = PureWhite

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .testTag("sci_btn_$text"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium
        )
    }
}
