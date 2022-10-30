package lab_1

import removeWhitespaces


fun main() {
    val secret = SECRET_PHRASE.removeWhitespaces()
    println("Secret phrase to encrypt - $secret")
    println("Column key - $COLUMN_KEY")
    println("Row key - $ROW_KEY")
    println("Matrix size: ${COLUMN_KEY.length}x${ROW_KEY.length}")

    val encryptedSecret = Encryptor().encrypt(secret)
    println("Encrypted secret phrase - $encryptedSecret")

    val decryptedPhrase = Decryptor().decrypt(encryptedSecret)
    println("Decrypted secret phrase - $decryptedPhrase")
}

