const val SALT = "|"
const val SPLITTER = "-"


fun String.removeWhitespaces() = replace(" ", "")
fun String.removeSalt() = replace(SALT, " ")
fun String.removeBrackets() = replace("]", "").replace("[", "")

fun applyShuffleAlgorithm(map: Map<Char, String>) = map.toSortedMap()
