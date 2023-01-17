package lab_3_DES


fun main() {
    val inputPhrase = "Vitalii Khorobchuk"
    println("Secret phrase to encrypt - $inputPhrase")
    DesEncryptorDecryptor.generateSymmetricKey()

    val encryptedSecret = DesEncryptorDecryptor.encrypt(inputPhrase.toByteArray(), DesEncryptorDecryptor.rawKey)
    println("Encrypted secret phrase - $encryptedSecret")

    val decryptedPhrase = DesEncryptorDecryptor.decrypt(encryptedSecret, DesEncryptorDecryptor.rawKey)
    println("Decrypted secret phrase byte - $decryptedPhrase")
    println("Decrypted secret phrase - ${String(decryptedPhrase)}")
}