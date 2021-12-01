import java.io.File

val changes = File(args[0])
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