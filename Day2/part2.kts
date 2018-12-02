import java.io.File

fun String.hamming(other: String) = zip(other).count { it.first != it.second}
fun String.removeDiff(other: String) = zip(other).fold("") { acc, pair -> 
    if (pair.first == pair.second) acc + pair.first else acc
}

val lines = File(args[0]).readLines().asSequence()

for ((index, line) in lines.withIndex()) {
    val otherLines = lines.take(index)
    val match = otherLines.find { it.hamming(line) == 1 }
    if (match != null) {
        val solution = line.removeDiff(match)
        println("Solution: $solution")
        break
    }
}