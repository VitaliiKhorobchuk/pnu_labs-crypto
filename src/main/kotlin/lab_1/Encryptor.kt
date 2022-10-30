package lab_1

import SALT
import SPLITTER
import applyShuffleAlgorithm
import removeSalt

class Encryptor {

    fun encrypt(secret: String): String {
        val columnMap = arrangeSecretByColumn(secret)
        val shuffledColumnMap = applyShuffleAlgorithm(columnMap)
        val rowMap = arrangeSecretByRow(shuffledColumnMap)
        val shuffledRowMap = applyShuffleAlgorithm(rowMap)

        val encryptedText = StringBuilder()
        shuffledRowMap.forEach {

            val cleanValue = it.value.removeSalt()
            encryptedText.append(cleanValue)
            if (shuffledRowMap.lastKey() != it.key) {
                encryptedText.append(SPLITTER)
            }
        }
        return encryptedText.toString()
    }

    private fun arrangeSecretByColumn(secret: String): MutableMap<Char, String> {
        val originalMap = mutableMapOf<Char, String>()
        val keySize = COLUMN_KEY.length
        val secretSize = secret.length
        COLUMN_KEY.forEachIndexed { index, element ->
            var i = 0
            val sb = StringBuilder()
            while (
                secretSize > index + keySize * i
            ) {
                sb.append(secret[index + keySize * i])
                i++
            }

            originalMap[element] = sb.toString()
        }
        return originalMap
    }

    private fun arrangeSecretByRow(columnMap: Map<Char, String>): MutableMap<Char, String> {
        val rowMap = mutableMapOf<Char, String>()

        ROW_KEY.forEachIndexed { index, element ->

            val sb = StringBuilder()
            columnMap.forEach { entry ->
                val value = entry.value
                if (value.length >= index + 1) {
                    sb.append(value[index])
                } else {
                    sb.append(SALT)
                }
            }

            rowMap[element] = sb.toString()
        }
        return rowMap
    }
}