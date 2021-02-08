import java.math.BigInteger

fun main() {
    val a = BigInteger(readLine()!!)
    val b = BigInteger(readLine()!!)
    val sum  = a + b
    val tmpA = (sum / a).toDouble()
    val tmpB = (sum / b).toDouble()
    //val tmpSum = tmpA + tmpB
    println("${tmpA}  ${tmpB}")
    //println("${(tmpB.toBigDecimal() / tmpSum.toBigDecimal())}% ${(tmpA.toBigDecimal() / tmpSum.toBigDecimal())}%")
}