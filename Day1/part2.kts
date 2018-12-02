import java.io.File

val input = args[0]
val changes = File(input)
    .readLines()
    .map(String::toInt)

val frequencies = HashSet<Int>()
var i = 0
var frequency = 0
do {
    frequency += changes[i]
    i = (i + 1) % changes.size
} while (frequencies.add(frequency))
println("Solution: $frequency")