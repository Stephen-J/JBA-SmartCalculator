fun main() {
    val input = readLine()!!
    val tmp = input.split("?")
    if (tmp.size == 2) {
        val params = tmp[1]
            .split("&")
            .map {it.split("=")}
        for (param in params) {
            if (param[1] == "") {
                println("${param[0]} : not found")
            } else {
                println("${param[0]} : ${param[1]}")
            }
        }
        val password = params.firstOrNull {
            it[0] == "pass"
        }
        if (password != null && password[1] != "") println("password : ${password[1]}")
    }
}