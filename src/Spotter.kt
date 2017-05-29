import java.util.*
import java.util.regex.Pattern


fun main(args: Array<String>) {
    spotDiff(args[0])
}

fun spotDiff(message: String): String {
    val p = Pattern.compile("expected:<\\{(.+)\\}> but was\\:<\\{(.+)\\}>")
    val m = p.matcher(message)

    if (m.find()) {
        val expected = m.group(1)
        val actual = m.group(2)
        val msg = "$expected \n $actual"
        println(msg)

        val expectedMap = buildMap(expected)
        val actualMap = buildMap(actual)
        println("Different entries:")

        for ((k, v) in expectedMap) {
            val actualValue = actualMap[k]
            if (!v.equals(actualValue)) {
                println("Expected:\t$k=$v")
                println("Actual:\t\t$k=$actualValue")
            }
            actualMap.remove(k)
        }

        println("Unexpected entries:")

        actualMap.forEach(::println)

        return msg
    }
    return message
}

fun buildMap(message: String): MutableMap<String, String> {
    val map = TreeMap<String, String>()
    val pairs = message.split(", ")

    val (simple, complex) = pairs
            .map { it.split("=") }
            .filter { it.isNotEmpty() }
            .partition { it.size == 2 }

    simple.forEach { map.put(it[0], it[1]) }
    complex.forEach { map.put(it[0], it.drop(1).joinToString { "#" }) }

    return map
}




