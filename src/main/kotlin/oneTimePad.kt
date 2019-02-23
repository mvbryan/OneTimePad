import java.io.File
import java.io.InputStream
import java.security.SecureRandom
import java.util.Base64
import kotlin.experimental.xor

fun main(){

    //Read plainText file in and print the contents
    val file = File(ClassLoader.getSystemResource("plainTextInput.txt").file)
    val inputStream: InputStream = file.inputStream()

    val inputString = inputStream.bufferedReader().use {it.readText()}
    println("Plain text message is: $inputString")

    //Convert plainText String to byte array and print size of byte array
    val plainBytes = inputString.toByteArray()
    println("Size of the byte array is: ${plainBytes.size}")

    /*Generate a random byte array the same size as the plainText

    Note the use of SecureRandom Provides a "cyrptographically strong random number generator which
    complies with the statistical random number generator tests specified in FIPS 140-2 Security Requirements for
    Cryptographic Modules  (See javadoc for SecureRandom
    */

    //Generate key based on size of plainText and print key in Hex
    val startKeyGen = System.currentTimeMillis()
    val random = SecureRandom()
    val key = ByteArray(plainBytes.size)
    random.nextBytes(key)
    for (b in key)
        print(String.format("%02X", b))

    //Key file written out as pure bytes for convenience and base64 encoded
    File("key.txt").writeBytes(key)
    File("base64Key.txt").writeText(Base64.getEncoder().encodeToString(key))

    val keyGenTime = System.currentTimeMillis() - startKeyGen

    println()
    println("Key generation took: $keyGenTime milliseconds")

    //Create cipher text by taking each byte of the key and xor with the plainText byte
    val startEncrypt = System.currentTimeMillis()
    var cipherByte: Byte
    var cipherByteArray = ByteArray(key.size)
    for (i in 0..(key.size-1)) {
        cipherByte = key[i].xor(plainBytes[i])
        cipherByteArray[i]=cipherByte
    }

    //Cipher text written out as pure bytes for convenience and base64 encoded
    File("cipherText.txt").writeBytes(cipherByteArray)
    File("base64CipherText.txt").writeText(Base64.getEncoder().encodeToString(cipherByteArray))


    val encryptTime = System.currentTimeMillis() - startEncrypt

    println("Encryption took: $encryptTime milliseconds")

    //Create decrypted text by taking each byte of the key and xor with the cipherText byte
    val startDecrypt = System.currentTimeMillis()
    var plainTextByte: Byte
    var plainTextByteArray = ByteArray(key.size)
    for (i in 0..(key.size-1)){
        plainTextByte = key[i].xor(cipherByteArray[i])
        plainTextByteArray[i] = plainTextByte
    }

    //Decrypted Text written out in file and printed for convenience
    File("DecryptedText.txt").writeText(String(plainTextByteArray, Charsets.UTF_8))

    val decryptTime = System.currentTimeMillis() - startDecrypt

    println("Decryption took: $decryptTime milliseconds")
    
    println("The decrypted text is: ${String(plainTextByteArray, Charsets.UTF_8)}")


    //XOR the plain bytes and key






}