package calculator

//todo Clean this up


const val helpMessage = """
This program accepts a string of numbers and operations (+,-)
eg.  1 + 2 - 3

it accepts both unary and binary minus operator
eg. 1 - 3 + -1 
"""

enum class TokenType {
    PLUS,
    MINUS,
    MULTIPLICATION,
    DIVISION,
    UNKNOWN,
    WHITESPACE,
    NUMBER,
    EQUAL,
    VARIABLE,
    COMMAND,
    LEFT_PARENS,
    RIGHT_PARENS,
    POWER
}

val operators = listOf(TokenType.PLUS,TokenType.MINUS,TokenType.DIVISION,TokenType.MULTIPLICATION)
val operands = listOf(TokenType.NUMBER,TokenType.VARIABLE)

data class Token(val value : String,val type : TokenType,val start : Int) {
    val end = start + value.length

    companion object {
        private val varToken = "[a-zA-Z]+".toRegex()
        private val numberToken = "[-+]?\\d+".toRegex()
        private val commandToken = "/[a-zA-Z]+".toRegex()
        private val whitespaceToken = "\\s*".toRegex()
        private val powerToken = "^\\d+".toRegex()
        private val plusToken = "\\++".toRegex()
        private val minusToken = "-+".toRegex()
        val precedence = mapOf(
            TokenType.LEFT_PARENS to 1,
            TokenType.RIGHT_PARENS to 1,
            TokenType.POWER to 3,
            TokenType.MULTIPLICATION to 4,
            TokenType.DIVISION to 4,
            TokenType.PLUS to 5,
            TokenType.MINUS to 5,)

        private fun fromMatchResult(result : MatchResult,type : TokenType) : Token {
            return Token(result.value,type,result.range.first)
        }

        private fun nextToken(input : String,currentIndex : Int) : Token {
            return when (input[currentIndex].toLowerCase()) {
                '=' -> Token(input[currentIndex].toString(),TokenType.EQUAL,currentIndex)
                '-' -> {
                    val numToken = numberToken.find(input,currentIndex)
                    if (numToken != null && numToken.range.first == currentIndex){
                        fromMatchResult(numToken,TokenType.NUMBER)
                    } else {
                        fromMatchResult(minusToken.find(input,currentIndex)!!,TokenType.MINUS)
                    }
                }
                '+' -> fromMatchResult(plusToken.find(input,currentIndex)!!,TokenType.PLUS)
                ' ' -> fromMatchResult(whitespaceToken.find(input,currentIndex)!!,TokenType.WHITESPACE)
                in '0'..'9' -> fromMatchResult(numberToken.find(input,currentIndex)!!,TokenType.NUMBER)
                in 'a'..'z' -> fromMatchResult(varToken.find(input,currentIndex)!!,TokenType.VARIABLE)
                '/' -> {
                    if (currentIndex == 0) {
                        fromMatchResult(commandToken.find(input,currentIndex)!!,TokenType.COMMAND)
                    } else Token(input[currentIndex].toString(),TokenType.DIVISION,currentIndex)
                }
                '(' -> Token(input[currentIndex].toString(),TokenType.LEFT_PARENS,currentIndex)
                ')' -> Token(input[currentIndex].toString(),TokenType.RIGHT_PARENS,currentIndex)
                '^' -> fromMatchResult(powerToken.find(input,currentIndex)!!,TokenType.POWER)
                '*' -> Token(input[currentIndex].toString(),TokenType.MULTIPLICATION,currentIndex)
                else -> Token(input.substring(currentIndex),TokenType.UNKNOWN,currentIndex)
            }
        }

        fun tokenize(input : String) : List<Token> {
            val tokens = mutableListOf<Token>()
            var currentIndex = 0
            while (currentIndex < input.length) {
                val token = nextToken(input,currentIndex)
                if (token.type != TokenType.WHITESPACE) tokens += token
                currentIndex = token.end
            }
            return tokens
        }
    }
}



fun convertToPostfix(tokens : List<Token>) : Pair<List<Token>,String> {
    val stack = ArrayDeque<Token>()
    val result = mutableListOf<Token>()
    var valid = true
    var bracketLevel = 0
    for (token in tokens) {
        when (token.type) {
            in operands -> result += token
            in operators -> when {
                stack.isEmpty() || stack.last().type == TokenType.LEFT_PARENS -> stack.addLast(token)
                Token.precedence[stack.last().type]!! > Token.precedence[token.type]!! -> stack.addLast(token)
                Token.precedence[stack.last().type]!! <= Token.precedence[token.type]!! -> {
                    while(stack.isNotEmpty() && (Token.precedence[stack.last().type]!! > Token.precedence[token.type]!! || stack.last().type != TokenType.LEFT_PARENS)) {
                        result += stack.removeLast()
                    }
                    stack.addLast(token)
                }
            }
            TokenType.LEFT_PARENS -> {
                stack.addLast(token)
                bracketLevel++
            }
            TokenType.RIGHT_PARENS -> {
                if (bracketLevel > 0) {
                    while(stack.last().type != TokenType.LEFT_PARENS) {
                        result += stack.removeLast()
                    }
                    stack.removeLast()
                    bracketLevel--
                } else valid = false
            }
        }
    }
    if (bracketLevel != 0) valid = false
    return if (!valid) {
        result.toList() to "Invalid expression"
    } else {
        while (stack.isNotEmpty()) result += stack.removeLast()
        result.toList() to ""
    }
}

fun evaluatePostfix(tokens : List<Token>,vars : MutableMap<String,Long>) : String {
    val stack = ArrayDeque<Long>()
    var error = ""
    try {
        for (token in tokens) {
            when (token.type) {
                TokenType.NUMBER -> stack.addLast(token.value.toLong())
                TokenType.VARIABLE -> {
                    val value = vars[token.value]
                    if (value != null) stack.addLast(value) else {
                        error = "Unknown variable"
                        break
                    }
                }
                in operators -> {
                    val b = stack.removeLast()
                    val a = stack.removeLast()
                    when (token.type) {
                        TokenType.MULTIPLICATION -> stack.addLast(a * b)
                        TokenType.DIVISION -> stack.addLast(a / b)
                        TokenType.MINUS -> if (token.value.length % 2 == 0) {
                            stack.addLast(a + b)
                        } else stack.addLast(a - b)
                        TokenType.PLUS -> stack.addLast(a + b)
                    }
                }
            }
        }
    } catch (ex : Exception) {
        error = "Invalid Expression"
    }
    if (stack.size != 1) error = "Invalid Expression"
    return if (error.isEmpty()) stack.removeLast().toString() else error
}

fun handleCommand(command: Token) : String {
    return when (command.value) {
        "/exit" -> "Bye!"
        "/help" -> helpMessage
        else -> "Unknown Command"
    }
}

fun handleAssignment(tokens : List<Token>,vars : MutableMap<String,Long>) : String? {
    var result : String? = "Invalid Assignment"
    if (tokens.size == 3){
        when (tokens[2].type) {
            TokenType.NUMBER -> {
                vars[tokens[0].value] = tokens[2].value.toLong()
                result = null
            }
            TokenType.VARIABLE -> {
                val v = vars[tokens[2].value]
                if (v != null) {
                    vars[tokens[0].value] = v
                    result = null
                } else result = "Unknown variable"
            }
            else -> result = "Invalid Assignment"
        }
    }
    return result
}

fun main() {
    val vars = mutableMapOf<String,Long>()
    val isCommand = {tokens : List<Token> ->
        tokens.size == 1 && tokens[0].type == TokenType.COMMAND
    }
    val isAssignment = {tokens : List<Token> ->
        tokens.any {it.type == TokenType.EQUAL}
    }
    do {
        val input = readLine()!!.trim()
        if (input.isNotEmpty()) {
            val tokens = Token.tokenize(input)
            when {
                isCommand(tokens) -> println(handleCommand(tokens[0]))
                isAssignment(tokens) -> {
                    val result = handleAssignment(tokens, vars)
                    if (result != null) println(result)
                }
                else -> {
                    val postfixed = convertToPostfix(tokens)
                    if (postfixed.second.isEmpty()) {
                        println(evaluatePostfix(postfixed.first,vars))
                    } else {
                        println(postfixed.second)
                    }
                }
            }
        }
    } while (input != "/exit")
}