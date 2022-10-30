package lab_1

import SPLITTER
import applyShuffleAlgorithm

class Decryptor {

    fun decrypt(toDecrypt: String): String {
        val decryptedText = StringBuilder()
        val split = toDecrypt.split(SPLITTER)

        // restore text by rows
        val decryptedRowMap = decryptByRow(split)
        // restore text by columns
        val decryptedColumnMap = decryptByColumn(decryptedRowMap)

        // restore text from matrix to words
        val words = mutableMapOf<Int, String>()
        decryptedColumnMap.forEach {
            it.value.forEachIndexed { index, c ->
                words[index] = (if(words.containsKey(index)) words[index] + c else c.toString())
            }
        }
        // make final original phrase
        words.forEach {
            decryptedText.append(it.value)
        }
        return decryptedText.toString().trim()
    }

    private fun decryptByRow(split: List<String>): Map<Char, String> {
        val rowMap = mutableMapOf<Char, String>()
        ROW_KEY.forEach {
            rowMap[it] = ""
        }

        val rowMapEncrypted = applyShuffleAlgorithm(rowMap)
        var index = 0
        rowMapEncrypted.forEach {
            rowMapEncrypted[it.key] = split[index]
            index++
        }

        rowMap.forEach {
            rowMap[it.key] = rowMapEncrypted[it.key]!!
        }

        return rowMap
    }

    private fun decryptByColumn(decryptedRowMap: Map<Char, String>): Map<Char, String> {
        val columnMap = mutableMapOf<Char, String>()
        COLUMN_KEY.forEach {
            columnMap[it] = ""
        }
        val columnMapEncrypted = applyShuffleAlgorithm(columnMap)

        var index = 0
        columnMapEncrypted.forEach {
            val sb = StringBuilder()
            for (element in ROW_KEY) {
                sb.append(decryptedRowMap[element]!![index])
            }
            columnMapEncrypted[it.key] = sb.toString()
            index++
        }

        columnMap.forEach {
            columnMap[it.key] = columnMapEncrypted[it.key]!!
        }

        return columnMap
    }
}