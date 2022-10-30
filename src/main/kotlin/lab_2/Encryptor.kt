package lab_2

import removeBrackets
import java.lang.StringBuilder

class Encryptor {

    fun encrypt(secret: String, cypherTable: MutableMap<Char, Pair<Int, Int>>): String {
        val encryptedText = StringBuilder()

        val textCoordinates = mutableListOf<Pair<Int, Int>>()
        secret.forEach {
            textCoordinates.add(cypherTable[it]!!)
        }

        println("Original coordinates $textCoordinates")
        val mixedCoordinates = mutableListOf<Pair<Int, Int>>()

        // mix coordinates
        val size = textCoordinates.size
        val isOdd = size % 2 == 0
        for (i in 0 until size step 2) {
            if (!isOdd && i == size - 1) {
                mixedCoordinates.add(Pair(textCoordinates[i].first, textCoordinates[i].second))
            } else {
                mixedCoordinates.add(Pair(textCoordinates[i].first, textCoordinates[i + 1].first))
                mixedCoordinates.add(Pair(textCoordinates[i].second, textCoordinates[i + 1].second))
            }
        }

        println("Mixed coordinates $mixedCoordinates")

        mixedCoordinates.forEach { newCoordinate ->
            encryptedText.append(
                cypherTable.filterValues { it == newCoordinate}.keys
            )
        }

        return encryptedText.toString().removeBrackets()
    }

}