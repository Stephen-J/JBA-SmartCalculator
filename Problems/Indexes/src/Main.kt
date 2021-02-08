fun solution(products: List<String>, product: String) {
    val indexes = mutableListOf<Int>()
    for ((i,str) in products.withIndex()) {
        if (str == product) indexes += i
    }
    println(indexes.joinToString(" "))
}