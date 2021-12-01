import java.io.File

fun String.hasDuplicateLetters(count: Int) = groupBy { it }
        .values
        .any { it.size == count}

val lines = File(args[0]).readLines()
val doubles = lines.filter { it.hasDuplicateLetters(2) }.count()
val tripples = lines.filter { it.hasDuplicateLetters(3) }.count()
println("Solution: ${doubles * tripples}")