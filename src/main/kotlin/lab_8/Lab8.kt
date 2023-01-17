package lab_8

import java.math.BigInteger
import java.security.SecureRandom
import java.util.*

fun main() {

    println("Diffie Hellman algorithm:")
    println("__________________________")
    DiffieHellmanAlgorithmExample().init()

    println("__________________________")
    println("ElGamalEncryption/Decryption:")
    println("__________________________")
    ElGamalEncryptorDecryptor().init()
}

class DiffieHellmanAlgorithmExample {
    fun init() {
        val P: Long
        val G: Long
        val x: Long
        val a: Long
        val y: Long
        val b: Long
        val ka: Long
        val kb: Long
        // create Scanner class object to take input from user
        val sc = Scanner(System.`in`)
        println("Both the users should be agreed upon the public keys G and P")
        // take inputs for public keys from the user
        println("Enter value for public key G:")
        G = sc.nextLong()
        println("Enter value for public key P:")
        P = sc.nextLong()
        // get input from user for private keys a and b selected by User1 and User2
        println("Enter value for private key a selected by user1:")
        a = sc.nextLong()
        println("Enter value for private key b selected by user2:")
        b = sc.nextLong()

        // call calculatePower() method to generate x and y keys
        x = calculatePower(G, a, P)
        y = calculatePower(G, b, P)
        // call calculatePower() method to generate ka and kb secret keys after the exchange of x and y keys
        // calculate secret key for User1
        ka = calculatePower(y, a, P)
        // calculate secret key for User2
        kb = calculatePower(x, b, P)
        // print secret keys of user1 and user2
        println("Secret key for User1 is:$ka")
        println("Secret key for User2 is:$kb")
    }

    // create calculatePower() method to find the value of x ^ y mod P
    private fun calculatePower(x: Long, y: Long, P: Long): Long {
        var result: Long = 0
        return if (y == 1L) {
            x
        } else {
            result = Math.pow(x.toDouble(), y.toDouble()).toLong() % P
            result
        }
    }
}

class ElGamalEncryptorDecryptor {

    fun init() {
        val p: BigInteger
        val b: BigInteger
        val c: BigInteger
        val secretKey: BigInteger
        val sc = SecureRandom()
        secretKey = BigInteger("12345678901234567890")
        //
        // public key calculation
        //
        println("secretKey = $secretKey")
        println("Generate P by Miller Rabin check")
//        MillerRabin().generate()
        p = BigInteger.probablePrime(64, sc)
        b = BigInteger("3")
        c = b.modPow(secretKey, p)
        println("p = $p")
        println("b = $b")
        println("c = $c")
        //
        // Encryption
        //
        print("Enter your Big Number message -->")
        val inputMessage = "12344123123124"
        val X = BigInteger(inputMessage)
        val r = BigInteger(64, sc)
        val EC = X.multiply(c.modPow(r, p)).mod(p)
        val brmodp = b.modPow(r, p)
        println("Plaintext = $X")
        println("r = $r")
        println("EC = $EC")
        println("b^r mod p = $brmodp")
        //
        // Decryption
        //
        val crmodp = brmodp.modPow(secretKey, p)
        val d = crmodp.modInverse(p)
        val ad = d.multiply(EC).mod(p)
        println("\n\nc^r mod p = $crmodp")
        println("d = $d")
        println("Decrypted: $ad")
    }
}

class MillerRabin {
    fun generate() {
        val min: Long = 2
        val max: Long = 1000000
        var i = min
        var j: Long = 1
        while (i <= max) {
            if (isPrime(i, 100)) {
                println(String.format("%s: %s", j++, i))
            }
            i++
        }
    }

    /**
     * Check if a number is a prime.
     *
     * @param candidate Number to be tested.
     * @param accuracy Number of tests to be performed.
     * @return true if the candidate is probably a prime, false if the candidate
     * is certainly no prime.
     */
    private fun isPrime(candidate: Long, accuracy: Long): Boolean {
        var d: Long
        var s: Long
        if (candidate == 2L) return true
        if (candidate < 2) return false

        // until d is odd
        d = 0
        s = 1
        while (d and 1L == 0L) {
            d = (candidate - 1) / fastPow(2, s)
            s++
        }
        verification@ for (i in 0 until accuracy) {
            // random base in the range [2, n-1]
            val base = (Math.random() * (candidate - 3) + 2).toLong()
            var x = fastPow(base, d, candidate)
            if (x == 1L || x == candidate - 1) continue@verification
            for (j in 0 until s - 1) {
                x = fastPow(x, 2, candidate)
                if (x == 1L) return false
                if (x == candidate - 1) continue@verification
            }
            return false
        }
        return true
    }

    /**
     * Returns the value of the first argument raised to the power of the second
     * argument.
     *
     * @param base
     * @param exponent
     * @return the value base^exponent
     */
    private fun fastPow(base: Long, exponent: Long): Long {
        var shift = 63 //  position
        var result = base // (1 * 1) * base = base

        // Skip all leading 0 bits and the most significant 1 bit.
        while (exponent shr shift-- and 1L == 0L);
        while (shift >= 0) {
            result *= result
            if (exponent shr shift-- and 1L == 1L) result *= base
        }
        return result
    }

    /**
     * Returns the value of the first argument raised to the power of the second
     * argument modulo the third argument.
     *
     * @param base
     * @param exponent
     * @param modulo
     * @return the value base^exponent % modulo
     */
    private fun fastPow(base: Long, exponent: Long, modulo: Long): Long {
        var shift = 63 // bit position
        var result = base // (1 * 1) * base = base

        // Skip all leading 0 bits and the most significant 1 bit.
        while (exponent shr shift-- and 1L == 0L);
        while (shift >= 0) {
            result = result * result % modulo
            if (exponent shr shift-- and 1L == 1L) result = result * base % modulo
        }
        return result
    }
}