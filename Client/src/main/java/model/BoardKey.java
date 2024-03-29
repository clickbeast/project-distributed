package model;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class BoardKey {
    private static byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static IvParameterSpec ivspec = new IvParameterSpec(iv);
    private String key;
    private String tag;
    private int nextSpot;

    public BoardKey(int range) {
        Random rnd = new Random();
        System.out.println(range);
        key = generateRandomString();
        tag = generateRandomString();
        if (range != 0) {
            nextSpot = rnd.nextInt(range);
        } else {
            nextSpot = 1;
        }
    }

    public BoardKey(String key, String tag, int nextSpot) {
        this.key = key;
        this.tag = tag;
        this.nextSpot = nextSpot;
    }

    public String encrypt(Message message, int bound, int next, String tag) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException {
        Random rnd = new Random();


        String stringToEncrypt = message.getText() + ":" + tag + ":" + next;
        stringToEncrypt = stringToEncrypt.replace("\n", " ").replace("\r", " ");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getEncryptKey());
        byte[] ciphertext = cipher.doFinal(stringToEncrypt.getBytes(StandardCharsets.ISO_8859_1));
        /*
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getEncryptKey());*/
        return new String(ciphertext, StandardCharsets.ISO_8859_1);

    }

    public Message decrypt(String text, int id) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException {


        /*
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, getEncryptKey());*/
        Cipher cipher2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher2.init(Cipher.DECRYPT_MODE, getEncryptKey());
        String[] decrypted = bytesToHex(cipher2.doFinal(text.getBytes(StandardCharsets.ISO_8859_1))).split(":");
        this.tag = decrypted[1];
        this.nextSpot = Integer.parseInt(decrypted[2]);
        return new Message(decrypted[0], id, System.currentTimeMillis(), false, true, false);


    }


    public String generateRandomString() {
        String AB = "0123456789abcdef";
        Random rnd = new Random();

        StringBuilder sb2 = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            sb2.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb2.toString();

    }

    public Key getEncryptKey() throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(key.toCharArray(), new byte[1], 1000, 128);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");

      /*  PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), new byte[1], 1000, 128);
        return new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).getEncoded()
                , "AES");
*/




       /* SecureRandom random = new SecureRandom(key.getBytes());
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(random);

        //Creating/Generating a key
        return keyGen.generateKey();*/
    }


    public String getKey() {
        return key;
    }

    public void generateNextKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), new byte[1], 1000, 128 * 8);
        key = bytesToHex(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).getEncoded());
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getNextSpot() {
        return nextSpot;
    }

    public void setNextSpot(int nextSpot) {
        this.nextSpot = nextSpot;
    }

    public String bytesToHex(byte[] hash) {
        return new String(hash, StandardCharsets.ISO_8859_1);
      /*  StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();*/
    }

    @Override
    public String toString() {
        return "BoardKey{" +
                "key='" + key + '\'' +
                ", tag='" + tag + '\'' +
                ", nextSpot=" + nextSpot +
                '}';
    }

    public String toFileString() {
        return this.key + "," + this.tag + "," + this.nextSpot;
    }
}
