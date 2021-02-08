fun solution(numbers: List<Int>, number: Int): MutableList<Int> {
    val mut = numbers.toMutableList()
    mut += number
    return mut
}