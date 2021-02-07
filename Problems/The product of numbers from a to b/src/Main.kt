fun main() {
    val from = readLine()!!.toInt()
    val to = readLine()!!.toInt()
    var result = 1L
    for (n in from until to) {
        result *= n
    }
    println(result)
}