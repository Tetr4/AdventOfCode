import java.io.File

val result = File(args[0])
    .readLines()
    .map(String::toInt)
    .sum()
println("Solution: $result")