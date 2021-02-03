fun main() {
    val vowels = charArrayOf('a','e','i','o','u','y')
    val input = readLine()!!.toLowerCase()
    var tmpCount = 1
    var num = 0
    var tmpNum = 0
    var lookingForVowel = input[0] in vowels
    var haveVowel : Boolean
    for (ch in input.substring(1)) {
        haveVowel = ch in vowels
        if (haveVowel == lookingForVowel) {
            tmpCount++
            tmpNum = if (tmpCount % 2 == 0 ) {
                tmpCount / 2 - 1
            } else tmpCount / 2
        } else {
            num += tmpNum
            tmpNum = 0
            tmpCount = 1
        }
        lookingForVowel = haveVowel
    }
    println(num + tmpNum)
}
