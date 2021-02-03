fun main() {
    val input = readLine()!!.split(" ")
    val s = input[0]
    val n = input[1].toInt()
    println(if (n > s.length) s else "${s.substring(n)}${s.substring(0,n)}")
}