fun main() {
    val letter = readLine()!!.toLowerCase().first()
    val vowels = mutableMapOf(
        'a' to 1,
        'e' to 5,
        'i' to 9,
        'o' to 15,
        'u' to 21
    )
    println(vowels[letter] ?: 0)
}