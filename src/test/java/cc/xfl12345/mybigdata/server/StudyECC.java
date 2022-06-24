package cc.xfl12345.mybigdata.server;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.cipher.CryptoCipherFactory;
import org.apache.commons.crypto.utils.Utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class StudyECC {
    public static void main(String[] args) throws Exception {
        // final SecretKeySpec key = new SecretKeySpec(getUTF8Bytes("1234567890123456"), "AES");
        // final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes("1234567890123456"));
        //
        // final Properties properties = new Properties();
        // properties.setProperty(CryptoCipherFactory.CLASSES_KEY, CryptoCipherFactory.CipherProvider.OPENSSL.getClassName());
        // //Creates a CryptoCipher instance with the transformation and properties.
        // final String transform = "AES/CBC/PKCS5Padding";
        // final CryptoCipher encipher = Utils.getCipherInstance(transform, properties);
        // System.out.println("Cipher:  " + encipher.getClass().getCanonicalName());
        //
        // final String sampleInput = "hello world!";
        // System.out.println("input:  " + sampleInput);
        //
        // final byte[] input = getUTF8Bytes(sampleInput);
        // final byte[] output = new byte[32];
        //
        // //Initializes the cipher with ENCRYPT_MODE, key and iv.
        // encipher.init(Cipher.ENCRYPT_MODE, key, iv);
        // //Continues a multiple-part encryption/decryption operation for byte array.
        // final int updateBytes = encipher.update(input, 0, input.length, output, 0);
        // System.out.println(updateBytes);
        // //We must call doFinal at the end of encryption/decryption.
        // final int finalBytes = encipher.doFinal(input, 0, 0, output, updateBytes);
        // System.out.println(finalBytes);
        // //Closes the cipher.
        // encipher.close();
        //
        // System.out.println(Arrays.toString(Arrays.copyOf(output, updateBytes + finalBytes)));
        //
        // // Now reverse the process using a different implementation with the same settings
        // properties.setProperty(CryptoCipherFactory.CLASSES_KEY, CryptoCipherFactory.CipherProvider.JCE.getClassName());
        // final CryptoCipher decipher = Utils.getCipherInstance(transform, properties);
        // System.out.println("Cipher:  " + encipher.getClass().getCanonicalName());
        //
        // decipher.init(Cipher.DECRYPT_MODE, key, iv);
        // final byte[] decoded = new byte[32];
        // decipher.doFinal(output, 0, updateBytes + finalBytes, decoded, 0);
        //
        // System.out.println("output: " + new String(decoded, StandardCharsets.UTF_8));


        // Security.addProvider(new SunEC());

        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator ecKeyGen = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
        ecKeyGen.initialize(new ECGenParameterSpec("secp521r1"));

        Instant startTime = Instant.now();
        KeyPair ecKeyPair = ecKeyGen.generateKeyPair();
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);

        System.out.println( "Cost = " +
            (duration.getSeconds() == 0L ?
                duration.getNano() + " nanosecond." :
                duration.getSeconds() + " second."
            ) +
            "What is slow?"
        );

        Cipher iesCipher = Cipher.getInstance("ECIESWITHSHA512");
        Cipher iesDecipher = Cipher.getInstance("ECIESWITHSHA512");
        iesCipher.init(Cipher.ENCRYPT_MODE, ecKeyPair.getPublic());

        String message = "Hello World";

        byte[] ciphertext = iesCipher.doFinal(message.getBytes());
        System.out.println(Hex.encodeHexString(ciphertext));

        iesDecipher.init(Cipher.DECRYPT_MODE, ecKeyPair.getPrivate(), iesCipher.getParameters());
        byte[] plaintext = iesDecipher.doFinal(ciphertext);

        System.out.println(new String(plaintext));
    }

    private static byte[] getUTF8Bytes(final String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }
}
