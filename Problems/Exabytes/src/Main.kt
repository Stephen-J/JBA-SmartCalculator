import java.math.BigInteger

fun main() {
    val input = BigInteger(readLine()!!)
    val exa = BigInteger("9223372036854775808")
    println(input * exa)
}