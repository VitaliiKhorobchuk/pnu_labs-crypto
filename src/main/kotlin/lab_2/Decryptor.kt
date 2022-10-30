package lab_2

import removeBrackets

class Decryptor {

    fun decrypt(secret: String, cypherTable: MutableMap<Char, Pair<Int, Int>>): String {
        val decryptedText = StringBuilder()

        val textCoordinates = mutableListOf<Pair<Int, Int>>()
        secret.forEach {
            textCoordinates.add(cypherTable[it]!!)
        }

        println("Encrypted coordinates $textCoordinates")
        val originalCoordinates = mutableListOf<Pair<Int, Int>>()

        // restore coordinates
        val size = textCoordinates.size
        val isOdd = size % 2 == 0
        for (i in 0 until size step 2) {
            if (!isOdd && i == size - 1) {
                originalCoordinates.add(Pair(textCoordinates[i].first, textCoordinates[i].second))
            } else {
                originalCoordinates.add(Pair(textCoordinates[i].first, textCoordinates[i + 1].first))
                originalCoordinates.add(Pair(textCoordinates[i].second, textCoordinates[i + 1].second))
            }
        }

        println("Original coordinates $originalCoordinates")

        originalCoordinates.forEach { newCoordinate ->
            decryptedText.append(
                cypherTable.filterValues { it == newCoordinate}.keys
            )
        }
        return decryptedText.toString().removeBrackets()
    }
}