package lab_9_md5

import java.security.MessageDigest

private val inputMessage = "Vitalii Khorobchuk"

fun main() {
    val md = MessageDigest.getInstance("MD5")
    md.update(inputMessage.toByteArray())
    println("Input data to hash : $inputMessage")

    val byteData = md.digest()

    // convert byte to 16 system
    val hexString = StringBuffer()
    for (aByteData in byteData) {
        val hex = Integer.toHexString(0xff and aByteData.toInt())
        if (hex.length == 1) hexString.append('0')
        hexString.append(hex)
    }
    println("MD5 hash result : $hexString")
}