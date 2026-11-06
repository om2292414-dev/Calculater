package com.example.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.Stack
import kotlin.math.*

data class HistoryItem(
    val id: Long = System.currentTimeMillis(),
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class AngleUnit {
    DEG, RAD
}

object CalculatorEngine {

    private val numberFormatter = DecimalFormat("#,##0.##########", DecimalFormatSymbols(Locale.US))

    fun formatNumber(value: Double): String {
        if (value.isNaN()) return "Error"
        if (value.isInfinite()) return if (value > 0) "Infinity" else "-Infinity"
        // If absolute value is extremely large or tiny, use scientific
        val absVal = abs(value)
        if (absVal != 0.0 && (absVal >= 1e12 || absVal < 1e-7)) {
            return String.format(Locale.US, "%.6e", value)
        }
        val rounded = BigDecimal(value).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros()
        return rounded.toPlainString()
    }

    /**
     * Evaluates a mathematical expression string.
     * Supports +, −, ×, ÷, %, ^, !, sin, cos, tan, asin, acos, atan, ln, log, √, π, e, (, )
     */
    fun evaluate(rawExpr: String, angleUnit: AngleUnit = AngleUnit.DEG): Result<Double> {
        val cleaned = prepareExpression(rawExpr)
        if (cleaned.isBlank()) return Result.failure(IllegalArgumentException("Empty expression"))

        return try {
            val tokens = tokenize(cleaned)
            val rpn = toRpn(tokens)
            val result = evaluateRpn(rpn, angleUnit)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun prepareExpression(expr: String): String {
        return expr
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("√", "sqrt")
            .replace("π", "PI")
            .replace("e", "E_CONST")
            .trim()
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        val len = expr.length

        while (i < len) {
            val c = expr[i]
            when {
                c.isWhitespace() -> i++
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < len && (expr[i].isDigit() || expr[i] == '.')) {
                        sb.append(expr[i])
                        i++
                    }
                    tokens.add(sb.toString())
                }
                c.isLetter() -> {
                    val sb = StringBuilder()
                    while (i < len && (expr[i].isLetter() || expr[i] == '_')) {
                        sb.append(expr[i])
                        i++
                    }
                    val word = sb.toString()
                    tokens.add(word)
                }
                c in "+-*/^%!()" -> {
                    // Check for unary minus
                    if (c == '-') {
                        val isUnary = tokens.isEmpty() ||
                                tokens.last() in listOf("+", "-", "*", "/", "^", "%", "(")
                        if (isUnary) {
                            tokens.add("neg")
                            i++
                            continue
                        }
                    }
                    tokens.add(c.toString())
                    i++
                }
                else -> i++
            }
        }

        // Insert implicit multiplications: e.g., 2(3) -> 2 * (3), (2)(3) -> (2) * (3), 2 PI -> 2 * PI
        val finalTokens = mutableListOf<String>()
        for (idx in 0 until tokens.size) {
            val current = tokens[idx]
            if (idx > 0) {
                val prev = tokens[idx - 1]
                val prevIsVal = isNumberOrConstOrCloseParen(prev)
                val currIsValOrFunc = isNumberOrConstOrOpenParenOrFunc(current)
                if (prevIsVal && currIsValOrFunc) {
                    finalTokens.add("*")
                }
            }
            finalTokens.add(current)
        }
        return finalTokens
    }

    private fun isNumberOrConstOrCloseParen(t: String): Boolean {
        if (t == ")" || t == "PI" || t == "E_CONST" || t == "%") return true
        return t.toDoubleOrNull() != null
    }

    private fun isNumberOrConstOrOpenParenOrFunc(t: String): Boolean {
        if (t == "(" || t == "PI" || t == "E_CONST") return true
        if (t in listOf("sin", "cos", "tan", "asin", "acos", "atan", "ln", "log", "sqrt", "√")) return true
        return t.toDoubleOrNull() != null
    }

    private fun precedence(op: String): Int {
        return when (op) {
            "+", "-" -> 1
            "*", "/", "%" -> 2
            "^" -> 3
            "neg" -> 4
            "!" -> 5
            "sin", "cos", "tan", "asin", "acos", "atan", "ln", "log", "sqrt", "√" -> 6
            else -> 0
        }
    }

    private fun isRightAssociative(op: String): Boolean = op == "^" || op == "neg"

    private fun toRpn(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val stack = Stack<String>()

        for (token in tokens) {
            when {
                token == "PI" || token == "E_CONST" || token.toDoubleOrNull() != null -> {
                    output.add(token)
                }
                token in listOf("sin", "cos", "tan", "asin", "acos", "atan", "ln", "log", "sqrt", "√", "neg") -> {
                    stack.push(token)
                }
                token == "!" || token == "%" -> {
                    // Postfix operators: pop operators with higher precedence
                    while (stack.isNotEmpty() && precedence(stack.peek()) > precedence(token)) {
                        output.add(stack.pop())
                    }
                    output.add(token)
                }
                token in listOf("+", "-", "*", "/", "^") -> {
                    while (stack.isNotEmpty() && stack.peek() != "(" &&
                        (precedence(stack.peek()) > precedence(token) ||
                                (precedence(stack.peek()) == precedence(token) && !isRightAssociative(token)))
                    ) {
                        output.add(stack.pop())
                    }
                    stack.push(token)
                }
                token == "(" -> {
                    stack.push(token)
                }
                token == ")" -> {
                    while (stack.isNotEmpty() && stack.peek() != "(") {
                        output.add(stack.pop())
                    }
                    if (stack.isNotEmpty() && stack.peek() == "(") {
                        stack.pop()
                    }
                    if (stack.isNotEmpty() && stack.peek() in listOf("sin", "cos", "tan", "asin", "acos", "atan", "ln", "log", "sqrt", "√")) {
                        output.add(stack.pop())
                    }
                }
                else -> {
                    // Treat as number or constant
                    output.add(token)
                }
            }
        }

        while (stack.isNotEmpty()) {
            output.add(stack.pop())
        }

        return output
    }

    private fun evaluateRpn(rpn: List<String>, angleUnit: AngleUnit): Double {
        val stack = Stack<Double>()

        for (token in rpn) {
            when (token) {
                "PI" -> stack.push(Math.PI)
                "E_CONST" -> stack.push(Math.E)
                "neg" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid negation")
                    stack.push(-stack.pop())
                }
                "%" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid percent")
                    stack.push(stack.pop() / 100.0)
                }
                "!" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid factorial")
                    val n = stack.pop()
                    if (n < 0 || n != floor(n) || n > 170) throw IllegalArgumentException("Invalid factorial input")
                    var fact = 1.0
                    for (k in 1..n.toInt()) fact *= k
                    stack.push(fact)
                }
                "sqrt", "√" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid sqrt")
                    val v = stack.pop()
                    if (v < 0) throw ArithmeticException("Square root of negative number")
                    stack.push(sqrt(v))
                }
                "ln" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid ln")
                    val v = stack.pop()
                    if (v <= 0) throw ArithmeticException("ln of non-positive")
                    stack.push(ln(v))
                }
                "log" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid log")
                    val v = stack.pop()
                    if (v <= 0) throw ArithmeticException("log of non-positive")
                    stack.push(log10(v))
                }
                "sin" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid sin")
                    val arg = if (angleUnit == AngleUnit.DEG) Math.toRadians(stack.pop()) else stack.pop()
                    stack.push(sin(arg))
                }
                "cos" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid cos")
                    val arg = if (angleUnit == AngleUnit.DEG) Math.toRadians(stack.pop()) else stack.pop()
                    stack.push(cos(arg))
                }
                "tan" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid tan")
                    val arg = if (angleUnit == AngleUnit.DEG) Math.toRadians(stack.pop()) else stack.pop()
                    stack.push(tan(arg))
                }
                "asin" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid asin")
                    val v = stack.pop()
                    val res = asin(v)
                    stack.push(if (angleUnit == AngleUnit.DEG) Math.toDegrees(res) else res)
                }
                "acos" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid acos")
                    val v = stack.pop()
                    val res = acos(v)
                    stack.push(if (angleUnit == AngleUnit.DEG) Math.toDegrees(res) else res)
                }
                "atan" -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid atan")
                    val v = stack.pop()
                    val res = atan(v)
                    stack.push(if (angleUnit == AngleUnit.DEG) Math.toDegrees(res) else res)
                }
                "+", "-", "*", "/", "^" -> {
                    if (stack.size < 2) throw IllegalArgumentException("Invalid binary operation")
                    val b = stack.pop()
                    val a = stack.pop()
                    val res = when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> {
                            if (b == 0.0) throw ArithmeticException("Cannot divide by zero")
                            a / b
                        }
                        "^" -> a.pow(b)
                        else -> 0.0
                    }
                    stack.push(res)
                }
                else -> {
                    val num = token.toDoubleOrNull() ?: throw IllegalArgumentException("Unknown token: $token")
                    stack.push(num)
                }
            }
        }

        if (stack.size != 1) throw IllegalArgumentException("Malformed expression")
        return stack.pop()
    }
}
