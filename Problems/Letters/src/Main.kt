fun main() {
    val letters = mutableMapOf<Int,Char>()
    var index = 1
    do {
        val ch = readLine()!!.first()
        letters[index] = ch
        index++
    } while (ch != 'z')
    print(letters[4])
}