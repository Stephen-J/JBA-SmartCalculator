package calculator


fun main() {
    val input = readLine()!!.split(" ").map {it.toInt()}
    val result = input.fold(0){sum,num -> sum + num}
    println(result)
}
