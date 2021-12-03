import java.io.File

fun List<String>.filterByChar(char: Char, index: Int) = filter { it[index] == char }

tailrec fun List<String>.findRating(common: Boolean, index: Int = 0): String {
    if (size == 1) {
        return first()
    } else {
        val ones = filterByChar('1', index)
        val zeroes = filterByChar('0', index)
        val moreOnes = ones.size >= zeroes.size
        val mostCommon = if (moreOnes) ones else zeroes
        val leastCommon = if (moreOnes) zeroes else ones
        val remaining = if (common) mostCommon else leastCommon
        return remaining.findRating(common, index+1)
    }
}

val lines = File(args[0]).readLines()
val oxy = lines.findRating(common = true).toInt(2)
val co2 = lines.findRating(common = false).toInt(2)
println("Solution: ${oxy * co2}")
