package calculator

const val helpMessage = """
This program accepts a string of numbers and operations (+,-)
eg.  1 + 2 - 3

it accepts both unary and binary minus operator
eg. 1 - 3 + -1 
"""

enum class Operation {
    PLUS,
    MINUS,
    NOOP
}

fun handleMath(input : String) : String {
    var currentOp = Operation.NOOP
    var result = 0
    var currentIndex = 0
    var valid = false
    var isFirstNumber = true
    while (currentIndex < input.length) {
        var ch = input[currentIndex]
        when (ch) {
            '+' -> {
                currentOp = Operation.PLUS
                valid = false
                currentIndex++
            }
            '-' -> {
                currentOp = if (isFirstNumber) Operation.MINUS else {
                    when (currentOp) {
                        Operation.PLUS -> Operation.MINUS
                        Operation.MINUS -> Operation.PLUS
                        Operation.NOOP -> Operation.MINUS
                    }
                }
                valid = false
                currentIndex++
            }
            ' ' -> currentIndex++
            in '0'..'9' -> {
                var tmpNum = ""
                while (ch in '0'..'9') {
                    tmpNum += ch
                    currentIndex++
                    if (currentIndex < input.length) {
                        ch = input[currentIndex]
                    } else break
                }
                if (tmpNum.isNotEmpty()) {
                    val num = tmpNum.toInt()
                    when (currentOp) {
                        Operation.PLUS -> result += num
                        Operation.MINUS -> result -= num
                        Operation.NOOP -> {
                            if (!isFirstNumber) {
                                valid = false
                                break
                            } else {
                                result += num
                                isFirstNumber = false
                            }
                        }

                    }
                    currentOp = Operation.NOOP
                    valid = true
                }
            }
            else -> currentIndex++
        }
    }
    return if (valid) result.toString() else "Invalid Expression"
}

fun handleCommand(command: String): String {
    return when (command) {
        "/exit" -> "Bye!"
        "/help" -> helpMessage
        else -> "Unknown Command"
    }
}

fun main() {
    do {
        val input = readLine()!!
        val result = when {
            input.startsWith("/") -> handleCommand(input)
            input.isNotEmpty() -> handleMath(input)
            else -> ""
        }
        if (result.isNotEmpty()) println(result)
    } while (input != "/exit")
}