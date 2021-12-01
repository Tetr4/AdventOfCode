import java.io.File

val count = File(args[0])
    .readLines()
    .map(String::toInt)
    .windowed(size=3)
    .map { it.sum() }
    .zipWithNext()
    .count { it.second > it.first}

println("Solution: $count")