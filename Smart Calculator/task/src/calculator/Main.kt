package calculator


const val helpMessage = """
This program accepts a string of numbers and operations (+,-)
eg.  1 + 2 - 3

it accepts both unary and binary minus operator
eg. 1 - 3 + -1 
"""

enum class TokenType {
    PLUS,
    MINUS,
    UNKNOWN,
    WHITESPACE,
    NUMBER,
    EQUAL,
    VARIABLE,
    COMMAND,
}

data class Token(val value : String,val type : TokenType,val start : Int) {
    val end = start + value.length

    companion object {
        private val varToken = "[a-zA-Z]+".toRegex()
        private val numberToken = "\\d+".toRegex()
        private val commandToken = "/[a-zA-Z]+".toRegex()
        private val whitespaceToken = "\\s*".toRegex()


        private fun fromMatchResult(result : MatchResult,type : TokenType) : Token {
            return Token(result.value,type,result.range.first)
        }

        private fun nextToken(input : String,currentIndex : Int) : Token {
            return when (input[currentIndex].toLowerCase()) {
                '=' -> Token(input[currentIndex].toString(),TokenType.EQUAL,currentIndex)
                '-' -> Token(input[currentIndex].toString(),TokenType.MINUS,currentIndex)
                '+' -> Token(input[currentIndex].toString(),TokenType.PLUS,currentIndex)
                ' ' -> fromMatchResult(whitespaceToken.find(input,currentIndex)!!,TokenType.WHITESPACE)
                in '0'..'9' -> fromMatchResult(numberToken.find(input,currentIndex)!!,TokenType.NUMBER)
                in 'a'..'z' -> fromMatchResult(varToken.find(input,currentIndex)!!,TokenType.VARIABLE)
                '/' -> fromMatchResult(commandToken.find(input,currentIndex)!!,TokenType.COMMAND)
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

fun handleExpression(tokens : List<Token>,vars : MutableMap<String,Long>) : String {
    var result = 0L
    var error = ""
    var currentOp = TokenType.PLUS
    val iterator = tokens.iterator()
    while (iterator.hasNext()) {
        val token = iterator.next()
        when (token.type) {
            TokenType.PLUS -> currentOp = TokenType.PLUS
            TokenType.MINUS -> {
                currentOp = when (currentOp) {
                    TokenType.PLUS -> TokenType.MINUS
                    TokenType.MINUS -> TokenType.PLUS
                    else -> TokenType.MINUS
                }
            }
            TokenType.VARIABLE -> {
                val v = vars[token.value]
                if (v != null) {
                    when (currentOp) {
                        TokenType.PLUS -> result += v
                        TokenType.MINUS -> result -= v
                        else -> {
                            error = "Invalid Expression"
                            break
                        }
                    }
                    currentOp = TokenType.UNKNOWN
                } else {
                    error = "Unknown variable"
                    break
                }
            }
            TokenType.NUMBER -> {
                when (currentOp) {
                    TokenType.PLUS -> result += token.value.toLong()
                    TokenType.MINUS -> result -= token.value.toLong()
                    else -> {
                        error = "Invalid Expression"
                        break
                    }
                }
                currentOp = TokenType.UNKNOWN
            }
            else -> {
                error = "Invalid Expression"
                break
            }
        }
    }
    return if (error.isEmpty()) result.toString() else error
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
        //tokens.size == 3 && tokens[1].type == TokenType.EQUAL
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
                else -> println(handleExpression(tokens,vars))
            }
        }
    } while (input != "/exit")
}