package com.example

import com.example.model.AngleUnit
import com.example.model.CalculatorEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testMathematicalPrecedence() {
        val result = CalculatorEngine.evaluate("2 + 3 × 4").getOrThrow()
        assertEquals(14.0, result, 0.0001)
    }

    @Test
    fun testDivisionAndSubtraction() {
        val result = CalculatorEngine.evaluate("20 ÷ 4 − 2").getOrThrow()
        assertEquals(3.0, result, 0.0001)
    }

    @Test
    fun testScientificFunctions() {
        val sin90 = CalculatorEngine.evaluate("sin(90)", AngleUnit.DEG).getOrThrow()
        assertEquals(1.0, sin90, 0.0001)

        val sqrt16 = CalculatorEngine.evaluate("√(16) + 5").getOrThrow()
        assertEquals(9.0, sqrt16, 0.0001)

        val power = CalculatorEngine.evaluate("2 ^ 3").getOrThrow()
        assertEquals(8.0, power, 0.0001)

        val fact = CalculatorEngine.evaluate("5!").getOrThrow()
        assertEquals(120.0, fact, 0.0001)
    }

    @Test
    fun testPercentage() {
        val result = CalculatorEngine.evaluate("50 × 20%").getOrThrow()
        assertEquals(10.0, result, 0.0001)
    }

    @Test
    fun testFormatNumber() {
        assertEquals("15", CalculatorEngine.formatNumber(15.0))
        assertEquals("3.1415926536", CalculatorEngine.formatNumber(Math.PI))
    }
}
