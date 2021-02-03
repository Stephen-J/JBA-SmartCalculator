package calculator


fun main() {
    do {
        val input = readLine()!!.split(" ")
        when {
            input.size == 2 -> println(input[0].toInt() + input[1].toInt())
            input.size == 1 && input[0] == "/exit" -> println("Bye!")
            input.size == 1 && input[0] != "/exit" && input[0] != "" -> println(input[0])

        }
    } while (input[0] != "/exit")
}