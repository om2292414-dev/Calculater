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
import kotlin.math.pow

@Composable
fun FinancialCalculatorsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    val modules = listOf("Loan / EMI", "SIP & Wealth", "Compound Interest", "GST & Sales Tax", "Tip & Split", "Discount")
    var selectedModule by remember { mutableStateOf("Loan / EMI") }
    val currencyFormat = remember { DecimalFormat("#,##0.00") }

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
                text = "Financial Calculators",
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
                    "Loan / EMI" -> LoanEmiCalculator(currencyFormat)
                    "SIP & Wealth" -> SipCalculator(currencyFormat)
                    "Compound Interest" -> CompoundInterestCalculator(currencyFormat)
                    "GST & Sales Tax" -> GstCalculator(currencyFormat)
                    "Tip & Split" -> TipSplitCalculator(currencyFormat)
                    "Discount" -> DiscountCalculator(currencyFormat)
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun LoanEmiCalculator(df: DecimalFormat) {
    var principalStr by remember { mutableStateOf("1000000") }
    var interestRateStr by remember { mutableStateOf("8.5") }
    var tenureYearsStr by remember { mutableStateOf("15") }

    val p = principalStr.toDoubleOrNull() ?: 0.0
    val annualRate = interestRateStr.toDoubleOrNull() ?: 0.0
    val tenureYears = tenureYearsStr.toDoubleOrNull() ?: 0.0

    val n = tenureYears * 12
    val r = (annualRate / 12) / 100

    val emi = if (p > 0 && r > 0 && n > 0) {
        val pow = (1 + r).pow(n)
        (p * r * pow) / (pow - 1)
    } else 0.0

    val totalPayment = emi * n
    val totalInterest = totalPayment - p

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Loan / Mortgage Monthly EMI Calculator", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        FinanceInput(label = "Loan Amount (Principal)", value = principalStr, onValueChange = { principalStr = it })
        FinanceInput(label = "Annual Interest Rate (% p.a.)", value = interestRateStr, onValueChange = { interestRateStr = it })
        FinanceInput(label = "Loan Tenure (Years)", value = tenureYearsStr, onValueChange = { tenureYearsStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Monthly EMI", value = "$ ${df.format(emi)}")
            ResultRow(title = "Total Interest Payable", value = "$ ${df.format(totalInterest)}")
            ResultRow(title = "Total Payment (Principal + Interest)", value = "$ ${df.format(totalPayment)}")
        }
    }
}

@Composable
private fun SipCalculator(df: DecimalFormat) {
    var monthlyInvestStr by remember { mutableStateOf("5000") }
    var expectedReturnStr by remember { mutableStateOf("12") }
    var timePeriodYearsStr by remember { mutableStateOf("10") }

    val p = monthlyInvestStr.toDoubleOrNull() ?: 0.0
    val annualRate = expectedReturnStr.toDoubleOrNull() ?: 0.0
    val years = timePeriodYearsStr.toDoubleOrNull() ?: 0.0

    val i = (annualRate / 12) / 100
    val n = years * 12

    val investedAmount = p * n
    val totalWealth = if (i > 0 && n > 0) {
        p * (((1 + i).pow(n) - 1) / i) * (1 + i)
    } else investedAmount
    val estimatedReturns = totalWealth - investedAmount

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("SIP (Systematic Investment Plan) Wealth Calculator", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        FinanceInput(label = "Monthly Investment Amount", value = monthlyInvestStr, onValueChange = { monthlyInvestStr = it })
        FinanceInput(label = "Expected Annual Return Rate (%)", value = expectedReturnStr, onValueChange = { expectedReturnStr = it })
        FinanceInput(label = "Time Horizon (Years)", value = timePeriodYearsStr, onValueChange = { timePeriodYearsStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Total Invested Amount", value = "$ ${df.format(investedAmount)}")
            ResultRow(title = "Estimated Wealth Gain", value = "$ ${df.format(estimatedReturns)}")
            ResultRow(title = "Total Future Maturity Value", value = "$ ${df.format(totalWealth)}")
        }
    }
}

@Composable
private fun CompoundInterestCalculator(df: DecimalFormat) {
    var principalStr by remember { mutableStateOf("10000") }
    var rateStr by remember { mutableStateOf("7") }
    var timeStr by remember { mutableStateOf("5") }

    val p = principalStr.toDoubleOrNull() ?: 0.0
    val r = (rateStr.toDoubleOrNull() ?: 0.0) / 100.0
    val t = timeStr.toDoubleOrNull() ?: 0.0

    val n = 12.0 // compounded monthly
    val amount = p * (1 + (r / n)).pow(n * t)
    val interest = amount - p

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Compound Interest (A = P(1 + r/n)^(nt))", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        FinanceInput(label = "Principal Amount", value = principalStr, onValueChange = { principalStr = it })
        FinanceInput(label = "Annual Interest Rate (%)", value = rateStr, onValueChange = { rateStr = it })
        FinanceInput(label = "Time Period (Years)", value = timeStr, onValueChange = { timeStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Total Interest Earned", value = "$ ${df.format(interest)}")
            ResultRow(title = "Final Compound Value", value = "$ ${df.format(amount)}")
        }
    }
}

@Composable
private fun GstCalculator(df: DecimalFormat) {
    var amountStr by remember { mutableStateOf("2500") }
    var gstRateStr by remember { mutableStateOf("18") }

    val amount = amountStr.toDoubleOrNull() ?: 0.0
    val rate = (gstRateStr.toDoubleOrNull() ?: 0.0) / 100.0

    val gstAdded = amount * rate
    val totalWithGst = amount + gstAdded

    val originalPreGst = amount / (1 + rate)
    val gstRemoved = amount - originalPreGst

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("GST / Sales Tax Calculator (+GST & -GST)", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        FinanceInput(label = "Amount", value = amountStr, onValueChange = { amountStr = it })
        FinanceInput(label = "GST / Tax Rate (%)", value = gstRateStr, onValueChange = { gstRateStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Adding GST (+GST):", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            ResultRow(title = "Tax Amount (+${gstRateStr}%)", value = "$ ${df.format(gstAdded)}")
            ResultRow(title = "Gross Total Amount", value = "$ ${df.format(totalWithGst)}")

            Spacer(modifier = Modifier.height(4.dp))
            Text("Removing GST (-GST):", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            ResultRow(title = "Net Pre-Tax Base Price", value = "$ ${df.format(originalPreGst)}")
            ResultRow(title = "Tax Component", value = "$ ${df.format(gstRemoved)}")
        }
    }
}

@Composable
private fun TipSplitCalculator(df: DecimalFormat) {
    var billStr by remember { mutableStateOf("120") }
    var tipPercentStr by remember { mutableStateOf("15") }
    var peopleStr by remember { mutableStateOf("4") }

    val bill = billStr.toDoubleOrNull() ?: 0.0
    val tipPct = (tipPercentStr.toDoubleOrNull() ?: 0.0) / 100.0
    val people = (peopleStr.toIntOrNull() ?: 1).coerceAtLeast(1)

    val tipAmount = bill * tipPct
    val totalBill = bill + tipAmount
    val perPerson = totalBill / people

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Tip & Bill Splitter", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        FinanceInput(label = "Bill Amount", value = billStr, onValueChange = { billStr = it })
        FinanceInput(label = "Tip Percentage (%)", value = tipPercentStr, onValueChange = { tipPercentStr = it })
        FinanceInput(label = "Number of People to Split", value = peopleStr, onValueChange = { peopleStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Tip Amount", value = "$ ${df.format(tipAmount)}")
            ResultRow(title = "Total Bill with Tip", value = "$ ${df.format(totalBill)}")
            ResultRow(title = "Each Person Pays", value = "$ ${df.format(perPerson)}")
        }
    }
}

@Composable
private fun DiscountCalculator(df: DecimalFormat) {
    var priceStr by remember { mutableStateOf("80") }
    var discountPctStr by remember { mutableStateOf("25") }

    val price = priceStr.toDoubleOrNull() ?: 0.0
    val discPct = (discountPctStr.toDoubleOrNull() ?: 0.0) / 100.0

    val savings = price * discPct
    val finalPrice = price - savings

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CharcoalCard)
            .border(1.dp, CharcoalCardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Discount & Savings Calculator", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        FinanceInput(label = "Original Price", value = priceStr, onValueChange = { priceStr = it })
        FinanceInput(label = "Discount Percentage (%)", value = discountPctStr, onValueChange = { discountPctStr = it })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CharcoalButton)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResultRow(title = "Discount Savings", value = "$ ${df.format(savings)}")
            ResultRow(title = "Final Price After Discount", value = "$ ${df.format(finalPrice)}")
        }
    }
}

@Composable
private fun FinanceInput(
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
