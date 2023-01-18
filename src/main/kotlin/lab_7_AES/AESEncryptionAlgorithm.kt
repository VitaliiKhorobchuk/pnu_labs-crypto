package lab_7_AES

fun main() {

    val inputText = "Sometesttext1234"
    val key = "sometestkey"
    println("AES-128 algorithm")

    val aes = AESEncryptionAlgorithm()

    val encrypt = aes.encrypt(
        inputText.toByteArray(),
        key.toByteArray()
    )

    println("Encrypt result $encrypt")
    println("Encrypt result str ${String(encrypt)}")
}

class AESEncryptionAlgorithm {
    private val nB = 4
//    private val Nk = 4
    private val nR = 10

    private val rCon = getRCon(10) // for AES - 128

    private var state = Array(4) { ByteArray(nB) }
    private var expandedKey = Array(4) { ByteArray(nB * (nR + 1)) }

    fun encrypt(plaintext: ByteArray, key: ByteArray): ByteArray {
        val ciphertext = ByteArray(plaintext.size)
//        expandedKey = keyExpansion(key)
        var block: ByteArray
        var i = 0
        while (i < plaintext.size) {
            block = plaintext.copyOfRange(i, i + 16)
            state = toStateArray(block)
            addRoundKey(0)
            for (round in 1 until nR) {
                subBytes()
                shiftRows()
                mixColumns()
                addRoundKey(round)
            }
            subBytes()
            shiftRows()
            addRoundKey(nR)
            block = toBlockArray(state)
            System.arraycopy(block, 0, ciphertext, i, block.size)
            i += 16
        }
        return ciphertext
    }

    private fun addRoundKey(round: Int) {
        for (i in 0..3) {
            for (j in 0..3) {
                state[i][j] = (state[i][j].toInt() xor expandedKey[i][round * 4 + j].toInt()).toByte()
            }
        }
    }

    private fun subBytes() {
        for (i in 0..3) {
            for (j in 0..3) {
                state[i][j] = sbox[state[i][j].toInt() shr 4 and 0x0f][state[i][j].toInt() and 0x0f]
            }
        }
    }

    private fun shiftRows() {
        for (i in 1..3) {
            state[i] = leftRotate(state[i], i)
        }
    }

    private fun leftRotate(input: ByteArray, offset: Int): ByteArray {
        val len = input.size
        val copy = ByteArray(len)
        System.arraycopy(input, 0, copy, 0, len)
        for (i in 0 until len) {
            val newI = (i + offset) % len
            input[i] = copy[newI]
        }
        return copy
    }


    private fun mixColumns() {
        for (i in 0..3) {
            var column = ByteArray(4)
            for (j in 0..3) {
                column[j] = state[j][i]
            }
            column = mixColumn(column)
            for (j in 0..3) {
                state[j][i] = column[j]
            }
        }
    }

    private fun mixColumn(column: ByteArray): ByteArray {
        val mixedColumn = ByteArray(4)
        mixedColumn[0] = (mul(2, column[0]).toInt()
                xor mul(3, column[1]).toInt()
                xor column[2].toInt()
                xor column[3].toInt()).toByte()
        mixedColumn[1] = (column[0].toInt()
                xor mul(2, column[1]).toInt()
                xor mul(3, column[2]).toInt()
                xor column[3].toInt()).toByte()
        mixedColumn[2] = (column[0].toInt() xor column[1].toInt()
                xor mul(2, column[2]).toInt()
                xor mul(3, column[3]).toInt()).toByte()
        mixedColumn[3] = (mul(3, column[0]).toInt()
                xor column[1].toInt()
                xor column[2].toInt()
                xor mul(2, column[3]).toInt()).toByte()
        return mixedColumn
    }

    private fun mul(a: Int, b: Byte): Byte {
        val indA = if (a < 0) a + 256 else a
        val indB = if (b < 0) b + 256 else b.toInt()
        return if (a != 0 && b.toInt() != 0) {
            println("a ${LogTable[indA]}")
            println("b ${LogTable[indB]}")
            println("sum ${LogTable[indB] + LogTable[indA]}")
            val index = LogTable[indA] + LogTable[indB]

            getAlogTable()[index % 255].toByte()
        } else 0
    }


    // Key Expansion

    private fun keyExpansion(key: ByteArray): ByteArray {
        val expandedKey = ByteArray(176)
        System.arraycopy(key, 0, expandedKey, 0, key.size)
        var bytesGenerated = key.size
        var rconIteration = 1
        var temp = ByteArray(4)
        while (bytesGenerated < expandedKey.size) {
            for (i in 0..3) {
                temp[i] = expandedKey[i + bytesGenerated - 4]
            }
            if (bytesGenerated % key.size == 0) {
                temp = subWord(rotWord(temp))
                temp[0] = (temp[0].toInt() xor rCon[rconIteration++]).toByte()
            }
            for (i in 0..3) {
                expandedKey[bytesGenerated] =
                    (expandedKey[bytesGenerated - key.size].toInt() xor temp[i].toInt()).toByte()
                bytesGenerated++
            }
        }
        return expandedKey
    }


    private fun subWord(word: ByteArray): ByteArray {
        for (i in 0 until nB) {
            word[i] = sbox[word[i].toInt() and 0xff shr 4][word[i].toInt() and 0x0f]
        }
        return word
    }

    private fun rotWord(input: ByteArray): ByteArray {
        val firstByte = input[0]
        System.arraycopy(input, 1, input, 0, input.size - 1)
        input[input.size - 1] = firstByte
        return input
    }

    private fun toStateArray(input: ByteArray): Array<ByteArray> {
        val state = Array(4) { ByteArray(4) }
        for (i in 0..3) {
            for (j in 0..3) {
                state[j][i] = input[i * 4 + j]
            }
        }
        return state
    }

    private fun toBlockArray(state: Array<ByteArray>): ByteArray {
        val block = ByteArray(16)
        for (i in 0..3) {
            for (j in 0..3) {
                block[i * 4 + j] = state[j][i]
            }
        }
        return block
    }
}