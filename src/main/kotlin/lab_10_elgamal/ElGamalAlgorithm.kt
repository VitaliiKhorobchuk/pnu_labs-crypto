package lab_10_elgamal

import java.util.*
import kotlin.math.pow


fun main() {
    val sc = Scanner(System.`in`)
    println("Enter the value of p")
    val p = sc.nextLong()
    println("Enter the value of alpha")
    val alpha = sc.nextLong()
    println("Enter the value of m")
    val m = sc.nextLong()
    println("Enter the value of k")
    val k = sc.nextLong()
    val sign = SignAlgorithm(p, alpha, m, k)
    val v = verify(sign.p, sign.alpha, sign.beta, sign.m, sign.r, sign.s)
    v.verified()
}


internal class SignAlgorithm(var p: Long, var alpha: Long, c: Long, d: Long) {
    var beta: Long
    var m: Long
    var r: Long
    var s: Long
    var k: Long
    private val z: Long = 16

    init {
        beta = Math.pow(alpha.toDouble(), z.toDouble()).toLong() % p
        m = c
        k = d //createK();
        r = createR(alpha, k)
        s = createS()
    }

    private fun gcd(a: Long, b: Long): Long {
        return if (a < b) gcd(b, a) else if (a % b == 0L) b else gcd(b, a % b)
    }

    /*long createK() {
		long a = 2*(p-1);
		while(gcd(a,p-1)!=1) {
			a = (long) (Math.random()*(p-1));
		}
		return a;
	}*/
    private fun invK(): Long {
        for (x in 1 until p - 1) if (k * x % (p - 1) == 1L) return x
        return 1
    }

    private fun createR(b: Long, c: Long): Long {
        val a = Math.pow(b.toDouble(), c.toDouble()).toLong()
        return if (a < Math.pow(2.0, 36.0).toLong() - 1) a % p else  //(a==(long)Math.pow(2, 36)-1)
            createR(b, c / 2) * createR(b, c - c / 2) % p
    }

    private fun createS(): Long {
        val a = invK() * (m - z * r) % (p - 1)
        return if (a >= 0) a else a + p - 1
    }
}

internal class verify(var p: Long, var alpha: Long, var beta: Long, var m: Long, var r: Long, var s: Long) {
    /*long v1() {
      return ((((long)Math.pow(beta,r))*((long)(Math.pow(r, s))))%p);
  }*/
    private fun v1(b: Long, c: Long, d: Long, e: Long): Long {
        val a = b.toDouble().pow(c.toDouble()).toLong() * Math.pow(d.toDouble(), e.toDouble())
            .toLong()
        return if (a < 2.0.pow(36.0).toLong() - 1) a % p else  //(a==(long)Math.pow(2, 36)-1)
            v1(b, c / 2, d, e / 2) * v1(b, c - c / 2, d, e - e / 2) % p
    }

    fun v2(): Long {
        return alpha.toDouble().pow(m.toDouble()).toLong() % p
    }

    private fun v2(b: Long, c: Long): Long {
        val a = b.toDouble().pow(c.toDouble()).toLong()
        return if (a < 2.0.pow(36.0).toLong() - 1) a % p else  //(a==(long)Math.pow(2, 36)-1)
            v2(b, c / 2) * v2(b, c - c / 2) % p
    }

    fun verified() {
        if (v1(beta, r, r, s) == v2(alpha, m)) {
            println("Signature verified using ElGamal.")
            println("The value of v1 mod p: " + v1(beta, r, r, s))
            println("The value of v2 mod p: " + v2(alpha, m))
        } else {
            println("Signature missmatch")
            println("The value of v1 mod p: " + v1(beta, r, r, s))
            println("The value of v2 mod p: " + v2(alpha, m))
        }
    }
}