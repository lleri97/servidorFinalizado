package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import service.CompanyFacadeREST;

/**
 *
 * @author Fran
 */
public class EncryptionServerClass {
    
    private static Cipher cipherD;

    private final static String CRYPTO_METHOD = "RSA";
    private final static String OPCION_RSA= "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    private final static String PUBLIC_PATH = ResourceBundle.getBundle("files.KeysProperties").getString("public_key");
    private final static String PRIVATE_PATH = ResourceBundle.getBundle("files.KeysProperties").getString("private_key");
        private static final Logger LOGGER = Logger.getLogger(CompanyFacadeREST.class.getPackage() + "." + CompanyFacadeREST.class.getName());



    /**
     * Cifra un texto con RSA, modo ECB y padding PKCS1Padding (asimétrica) y lo
     * retorna
     *
     * @param mensaje El mensaje a cifrar
     * @return El mensaje cifrado
     */
    public String encryptText(String mensaje) {
        byte[] encodedMessage = null;
        try {
            LOGGER.info("Encriptando texto");
            // Clave pública
            InputStream in = null;
             byte[] publicKeyBytes = null;
            in = EncryptionServerClass.class.getClassLoader().getResourceAsStream(PUBLIC_PATH);
            publicKeyBytes = new byte[in.available()];
             in.read(publicKeyBytes);
             in.close();
            KeyFactory keyFactory = KeyFactory.getInstance(CRYPTO_METHOD);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(OPCION_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encodedMessage = cipher.doFinal(mensaje.getBytes());
        } catch (Exception e) {
            LOGGER.info("No se pudo encriptar el texto");
        }
        LOGGER.info("Texto encriptado con exito");
        return toHexadecimal(encodedMessage);
    }

    /**
     * Descifra un texto con RSA, modo ECB y padding PKCS1Padding (asimétrica) y
     * lo retorna
     *
     * @param encryptedMessage El mensaje a descifrar
     * @return El mensaje descifrado
     */
    public static String decryptText(String encryptedMessage) {
        String message = null;
        try {
            LOGGER.info("Desencrptando texto");
            InputStream in = null;
            byte[] privateKeyBytes = null;
            in = EncryptionServerClass.class.getClassLoader().getResourceAsStream(PRIVATE_PATH);
            privateKeyBytes = new byte[in.available()];
            in.read(privateKeyBytes);
            in.close();
            
            KeyFactory keyFactory = KeyFactory.getInstance(CRYPTO_METHOD);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            
            cipherD = Cipher.getInstance(OPCION_RSA);
            cipherD.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] messageInBytes = cipherD.doFinal(hexStringToByteArray(encryptedMessage));
            message = new String(messageInBytes);
            
        } catch (Exception ex) {
            LOGGER.info("No se pudo desencriptar el texto");
        }
        LOGGER.info("Texto desencriptado con exito");
        return message;
    }

    /**
     * Aplica SHA al texto pasado por parámetro
     *
     * @param texto texto hasheado
     * @return string
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

        } catch (NoSuchAlgorithmException e) {
            LOGGER.info("Error al hashear el texto");
        }
        LOGGER.info("texto hasheado exitosamente");
        return encoded;
    }

   /**
    * Metodo para pasar a exadecimal
    * @param resumen byte array a transofrmar
    * @return string transformado de byte array
    */
    static String toHexadecimal(byte[] resumen) {
        LOGGER.info("Transformando a hexadecimal");
        String HEX = "";
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1) {
                HEX += "0";
            }
            HEX += h;
        }
        LOGGER.info("Transformacon exitosa");
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
            LOGGER.info("Leyendo arhivo");
            ret = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            LOGGER.info("No se pudo leer el archivo");
        }
        LOGGER.info("Archivo leido con exito");
        return ret;
    }

    /**
     * Transformacion de string a byte array
     * @param s string a modificar
     * @return  byte array del string modificado
     */
    public static byte[] hexStringToByteArray(String s) {
        LOGGER.info("Transformando string");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        LOGGER.info("Transformacion exitosa");
        return data;
    }
        /**
         * Metodo para recibir la clave publica
         * @return la clave publica en hexadecimal
         * @throws IOException  cuando no consigue transofrmar
         */
        public static String getPublic() throws IOException{
            LOGGER.info("Buscando clave publica ");
        InputStream in = null;
        byte[] publicKeyBytes = null;
        in = EncryptionServerClass.class.getClassLoader().getResourceAsStream(PUBLIC_PATH);
        publicKeyBytes = new byte[in.available()];
        in.read(publicKeyBytes);
        in.close();
        LOGGER.info("Clave publica encontrada con exito");
        return toHexadecimal(publicKeyBytes);
    }

}
