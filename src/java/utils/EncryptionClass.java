package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import service.CompanyFacadeREST;

/**
 *
 * @author Fran
 */
public class EncryptionClass {
    
    private static final Logger LOGGER = Logger.getLogger(CompanyFacadeREST.class.getPackage() + "." + CompanyFacadeREST.class.getName());

    /**
     * Cifra un texto con RSA, modo ECB y padding PKCS1Padding (asimétrica) y lo
     * retorna
     *
     * @param mensaje El mensaje a cifrar
     * @return El mensaje cifrado
     */
    public byte[] encryptText(String mensaje) {
        byte[] encodedMessage = null;
        try {
            LOGGER.info("Encriptando texto");
            // Clave pública
            byte fileKey[] = fileReader("c:\\trastero\\EjemploRSA_Public.key");
            System.out.println("Tamaño -> " + fileKey.length + " bytes");
            
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(fileKey);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encodedMessage = cipher.doFinal(mensaje.getBytes());
        } catch (Exception e) {
            LOGGER.info("No se pudo encriptar el texto");
        }
        LOGGER.info("Texto encriptado con exito");
        return encodedMessage;
    }

    /**
     * Descifra un texto con RSA, modo ECB y padding PKCS1Padding (asimétrica) y
     * lo retorna
     *
     * @param mensaje El mensaje a descifrar
     * @return El mensaje descifrado
     */
    private byte[] decryptText(byte[] mensaje) {
        byte[] decodedMessage = null;
        try {
            LOGGER.info("Desencriptando texto");
            // Clave pública
            byte fileKey[] = fileReader("c:\\trastero\\EjemploRSA_Private.key");
            System.out.println("Tamaño -> " + fileKey.length + " bytes");
            
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(fileKey);
            PrivateKey privateKey = keyFactory.generatePrivate(pKCS8EncodedKeySpec);
            
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decodedMessage = cipher.doFinal(mensaje);
        } catch (Exception e) {
            LOGGER.info("No se pudo desencriptar el texto");
        }
        LOGGER.info("Texto desencriptado con exito");
        return decodedMessage;
    }

    /**
     * Aplica SHA al texto pasado por parámetro
     *
     * @param texto tipo string
     * @return string codificado
     */
    public String hashingText(String texto) {
        MessageDigest messageDigest = null;
        byte[] hash = null;
        String encoded = null;
        try {
            LOGGER.info("Hasheando texto");
            messageDigest = MessageDigest.getInstance("SHA");
            byte[] dataBytes = texto.getBytes(); // texto a bytes
            messageDigest.update(dataBytes);// se introduce texto en bytes a resumir
            hash = messageDigest.digest();// se calcula el resumen
            encoded = Base64.getEncoder().encodeToString(hash);
            
            System.out.println("Mensaje original: " + texto);
            System.out.println("Número de Bytes: " + messageDigest.getDigestLength());
            System.out.println("Algoritmo: " + messageDigest.getAlgorithm());
            System.out.println("Mensaje Resumen: " + new String(hash));
            System.out.println("Mensaje en Hexadecimal: " + Hexadecimal(hash));
            System.out.println("Proveedor: " + messageDigest.getProvider().toString());
            System.out.println("encoded: " + encoded);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.info("No se pudo hashear el texto");
        }
        LOGGER.info("Texto hasheado con exito");
        return encoded;
    }

    /**
     * *
     * Metodo que convierte array de bytes a string
     *
     * @param resumen byteArray a transformar
     * @return string transformado
     */
    static String Hexadecimal(byte[] resumen) {
        String HEX = "";
        LOGGER.info("PAsando byte array a hexadecimal");
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1) {
                HEX += "0";
            }
            HEX += h;
        }
        LOGGER.info("Conversion correcta");
        return HEX.toUpperCase();
    }

    /**
     * Retorna el contenido de un fichero
     *
     * @param path Path del fichero
     * @return El texto del fichero
     */
    private byte[] fileReader(String path) {
        byte ret[] = null;
        File file = new File(path);
        try {
            LOGGER.info("Leyendo fichero");
            ret = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            LOGGER.info("No se pudo leer el fichero");
        }
        LOGGER.info("Fichero leido con exito");
        return ret;
    }

    /**
     * Clase main
     *
     * @param args argumentos para inicializar
     */
    public static void main(String[] args) {
        EncryptionClass ejemploRSA = new EncryptionClass();
        byte[] mensajeCifrado = ejemploRSA.encryptText("Mensaje super secreto");
        System.out.println("Cifrado! -> " + new String(mensajeCifrado));
        System.out.println("Tamaño -> " + mensajeCifrado.length + " bytes");
        System.out.println("-----------");
        byte[] mensajeDescifrado = ejemploRSA.decryptText(mensajeCifrado);
        System.out.println("Descifrado! -> " + new String(mensajeDescifrado));
        System.out.println("-----------");
        
    }
}
