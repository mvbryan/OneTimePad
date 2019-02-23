import java.io.File
import java.io.InputStream
import java.security.SecureRandom
import java.util.*
import kotlin.experimental.xor

fun main(){
    println("hello world!!")

    //Read plainText file in
    val file = File(ClassLoader.getSystemResource("plainTextInput.txt").file)
    val inputStream: InputStream = file.inputStream()

    val inputString = inputStream.bufferedReader().use {it.readText()}
    println(inputString)

    //Convert plainText String to byte array
    val plainBytes = inputString.toByteArray()
    println(plainBytes.size)

    /*Generate a random byte array the same size as the plainText

    Note the use of SecureRandom Provides a "cyrptographically strong random number generator which
    complies with the statistical random number generator tests specified in FIPS 140-2 Security Requirements for
    Cryptographic Modules  (See javadoc for SecureRandom
    */
    val random = SecureRandom()
    val key = ByteArray(plainBytes.size)
    for ( a in key)
        print(String.format("%02X", a))
    println()
    println(key.hashCode())
    random.nextBytes(key)
    for (b in key)
        print(String.format("%02X", b))

    //Key file written out as pure bytes for convenience and base64 encoded
    File("key.txt").writeBytes(key)
    File("base64Key.txt").writeText(Base64.getEncoder().encodeToString(key))
    var cipherByte: Byte
    var cipherByteArray = ByteArray(key.size)
    for (i in 0..(key.size-1)) {
        cipherByte = key[i].xor(plainBytes[i])
        cipherByteArray[i]=cipherByte
    }

    File("cipherText.txt").writeBytes(cipherByteArray)
    File("base64CipherText.txt").writeText(Base64.getEncoder().encodeToString(cipherByteArray))

    var plainTextByte: Byte
    var plainTextByteArray = ByteArray(key.size)
    for (i in 0..(key.size-1)){
        plainTextByte = key[i].xor(cipherByteArray[i])
        plainTextByteArray[i] = plainTextByte
    }

    File("DecryptedText.txt").writeText(String(plainTextByteArray, Charsets.UTF_8))


    //XOR the plain bytes and key






}