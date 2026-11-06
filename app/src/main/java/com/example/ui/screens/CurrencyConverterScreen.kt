package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.ui.theme.DividerColor
import com.example.ui.theme.OrangeEquals
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import java.text.DecimalFormat
import java.util.Locale

data class Currency(val code: String, val name: String, val symbol: String, val rateToUsd: Double)

@Composable
fun CurrencyConverterScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    val currencies = remember {
        listOf(
            Currency("USD", "United States Dollar", "$", 1.0),
            Currency("EUR", "Euro", "€", 0.92),
            Currency("GBP", "British Pound", "£", 0.79),
            Currency("INR", "Indian Rupee", "₹", 86.40),
            Currency("JPY", "Japanese Yen", "¥", 152.30),
            Currency("CAD", "Canadian Dollar", "C$", 1.38),
            Currency("AUD", "Australian Dollar", "A$", 1.54),
            Currency("CHF", "Swiss Franc", "CHF", 0.89),
            Currency("CNY", "Chinese Yuan", "¥", 7.24),
            Currency("SGD", "Singapore Dollar", "S$", 1.34),
            Currency("AED", "UAE Dirham", "AED", 3.67),
            Currency("BRL", "Brazilian Real", "R$", 5.65),
            Currency("KRW", "South Korean Won", "₩", 1378.0),
            Currency("SAR", "Saudi Riyal", "SAR", 3.75),
            Currency("MXN", "Mexican Peso", "Mex$", 19.80)
        )
    }

    var baseCurrency by remember { mutableStateOf(currencies[0]) } // USD
    var targetCurrency by remember { mutableStateOf(currencies[3]) } // INR
    var inputAmountStr by remember { mutableStateOf("1") }
    var baseMenuExpanded by remember { mutableStateOf(false) }
    var targetMenuExpanded by remember { mutableStateOf(false) }

    val inputAmount = inputAmountStr.toDoubleOrNull() ?: 0.0
    val exchangeRate = targetCurrency.rateToUsd / baseCurrency.rateToUsd
    val convertedAmount = inputAmount * exchangeRate

    val df = remember { DecimalFormat("#,##0.00") }

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
                text = "Live Currency Converter",
                color = PureWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { /* simulated live refresh */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = "Refresh Rates",
                    tint = OrangeEquals
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Conversion Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CharcoalCard)
                        .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    // Base Currency selector & amount
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
                                    .clickable { baseMenuExpanded = true }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${baseCurrency.code} (${baseCurrency.symbol})",
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            DropdownMenu(
                                expanded = baseMenuExpanded,
                                onDismissRequest = { baseMenuExpanded = false },
                                modifier = Modifier.background(CharcoalCard)
                            ) {
                                currencies.forEach { curr ->
                                    DropdownMenuItem(
                                        text = { Text("${curr.code} - ${curr.name}", color = PureWhite) },
                                        onClick = {
                                            baseCurrency = curr
                                            baseMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = inputAmountStr,
                            onValueChange = { inputAmountStr = it },
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
                                .width(160.dp)
                                .testTag("currency_input")
                        )
                    }

                    // Swap Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(OrangeEquals)
                                .clickable {
                                    val temp = baseCurrency
                                    baseCurrency = targetCurrency
                                    targetCurrency = temp
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SwapVert,
                                contentDescription = "Swap Currencies",
                                tint = PureWhite,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Target Currency selector & converted amount
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
                                    .clickable { targetMenuExpanded = true }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${targetCurrency.code} (${targetCurrency.symbol})",
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            DropdownMenu(
                                expanded = targetMenuExpanded,
                                onDismissRequest = { targetMenuExpanded = false },
                                modifier = Modifier.background(CharcoalCard)
                            ) {
                                currencies.forEach { curr ->
                                    DropdownMenuItem(
                                        text = { Text("${curr.code} - ${curr.name}", color = PureWhite) },
                                        onClick = {
                                            targetCurrency = curr
                                            targetMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Text(
                            text = "${targetCurrency.symbol} ${df.format(convertedAmount)}",
                            color = OrangeEquals,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "1 ${baseCurrency.code} = ${String.format(Locale.US, "%.4f", exchangeRate)} ${targetCurrency.code}",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                Text(
                    text = "Quick Conversion Multipliers",
                    color = PureWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                val multipliers = listOf(5, 10, 20, 50, 100, 500, 1000)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CharcoalCard)
                        .border(1.dp, CharcoalCardBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    multipliers.forEach { mult ->
                        val result = mult * exchangeRate
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "$mult ${baseCurrency.code}",
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${targetCurrency.symbol} ${df.format(result)}",
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
