import java.math.BigInteger

fun main() {
    val a = BigInteger(readLine()!!)
    val b = BigInteger(readLine()!!)
    val c = BigInteger(readLine()!!)
    val d = BigInteger(readLine()!!)
    val result = -a * b + c - d
    println(result)
}