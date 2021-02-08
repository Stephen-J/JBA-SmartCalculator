fun solution(strings: List<String>, str: String): Int {
    return strings.fold(0){ count,it -> if (it == str) count + 1 else count}
}