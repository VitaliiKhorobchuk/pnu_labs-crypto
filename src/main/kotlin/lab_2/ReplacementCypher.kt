package lab_2

import removeWhitespaces


fun main() {
    val secret = SECRET_PHRASE.removeWhitespaces()
    println("Secret phrase to encrypt - $secret")
    println("Letter z is forbidden in secret phrase")
    val cypherTable = generateLetterTable()

    val encryptedSecret = Encryptor().encrypt(secret, cypherTable)
    println("Encrypted secret phrase - $encryptedSecret")

    val decryptedPhrase = Decryptor().decrypt(encryptedSecret, cypherTable)
    println("Decrypted secret phrase - $decryptedPhrase")
}

private fun generateLetterTable(): MutableMap<Char, Pair<Int, Int>> {
    println("Use english alphabet with $COLUMN_SIZE x $ROW_SIZE size")
    val cypherMap = mutableMapOf<Char, Pair<Int, Int>>()
    val alphabet = mutableListOf<Char>()
    for (letter in 'a'..'y') {
        alphabet.add(letter)
    }
    var letterIndex = 0
    for (i in 0 until COLUMN_SIZE) {
        for (j in 0 until ROW_SIZE) {
            cypherMap[alphabet[letterIndex++]] = Pair(i, j)
        }
    }

    printCypherTable(cypherMap)
    return cypherMap
}

private fun printCypherTable(cypherMap: MutableMap<Char, Pair<Int, Int>>) {
    println("Cypher table:")
    for (i in 0 until COLUMN_SIZE) {
        print("   $i ")
    }
    println()
    for (i in 0 until COLUMN_SIZE) {
        print("$i ")
        for (j in 0 until ROW_SIZE) {
            print("${ cypherMap.filterValues { it == Pair(i,j)}.keys}  ")

        }
        println()
    }
}

