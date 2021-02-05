package calculator

const val helpMessage = """
This program accepts a string of numbers and operations (+,-)
eg.  1 + 2 - 3

it accepts both unary and binary minus operator
eg. 1 - 3 + -1 
"""

enum class Operation {
    PLUS,
    MINUS
}

fun handleMath(input : String) : Int {
    var currentOp = Operation.PLUS
    var result = 0
    var currentIndex = 0
    while (currentIndex < input.length) {
        var ch = input[currentIndex]
        when (ch) {
            '+' -> currentOp = Operation.PLUS
            '-' -> currentOp = when (currentOp) {
                Operation.PLUS -> Operation.MINUS
                Operation.MINUS -> Operation.PLUS
            }
            ' ' -> Unit
            else -> {
                var tmpNum = ""
                while (ch != '+' && ch != '-' && ch != ' ') {
                    tmpNum += ch
                    currentIndex++
                    if (currentIndex < input.length) {
                        ch = input[currentIndex]
                    } else break
                }
                val num = tmpNum.toInt()
                when (currentOp) {
                    Operation.PLUS -> result += num
                    Operation.MINUS -> result -= num
                }
                currentOp = Operation.PLUS

            }
        }
        currentIndex++
    }
    return result
}


fun main() {
    do {
        val input = readLine()!!
        when (input) {
            "/exit" -> println("Bye!")
            "/help" -> println(helpMessage)
            "" -> Unit
            else -> println(handleMath(input))

        }
    } while (input != "/exit")
}