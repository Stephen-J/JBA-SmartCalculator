import java.math.BigInteger

fun main() {
    val a = BigInteger(readLine()!!)
    val b = BigInteger(readLine()!!)
    val lcm = (a * b).abs() / a.gcd(b)
    println(lcm)
}