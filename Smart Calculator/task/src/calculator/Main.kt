package calculator


fun main() {
    do {
        val input = readLine()!!.split(" ")
        when {
            input.size >= 2 -> println(input.fold(0){ accum,v -> accum + v.toInt() })
            input.size == 1 -> when (input[0]) {
                "/exit" -> println("Bye!")
                "/help" -> println("The program calculates the sum of numbers")
                "" -> Unit
                else -> println(input[0])
            }
        }
    } while (input[0] != "/exit")
}