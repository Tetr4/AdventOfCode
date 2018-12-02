import java.io.File

fun String.hamming(other: String) = zip(other).count { it.first != it.second}
fun String.intersect(other: String) = asIterable()
    .intersect(other.asIterable())
    .joinToString("")

val lines = File(args[0]).readLines().asSequence()
for ((index, line) in lines.withIndex()) {
    val otherLines = lines.take(index)
    val match = otherLines.find { it.hamming(line) == 1 }
    if (match != null) {
        val solution = line.intersect(match)
        println("Solution: $solution")
        break
    }
}