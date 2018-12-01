import java.io.File

val input = args[0]
File(input).useLines { lines ->
    val result = lines.fold(0) { acc, line -> acc + line.toInt() }
    print("Solution: $result")
}