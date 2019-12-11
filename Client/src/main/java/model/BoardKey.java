package model;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class BoardKey {
    private String key;
    private String tag;
    private int nextSpot;

    public BoardKey(int range) {
        String AB = "0123456789abcdef";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        key = sb.toString();
        StringBuilder sb2 = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            sb2.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        tag = sb.toString();
        nextSpot = rnd.nextInt(range);
    }

    public BoardKey(String key, String tag, int nextSpot) {
        this.key = key;
        this.tag = tag;
        this.nextSpot = nextSpot;
    }

    public Key getEncryptKey() throws NoSuchAlgorithmException, InvalidKeySpecException {

        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), new byte[1], 1000, 128 * 8);
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec);







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

    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public String toString() {
        return "BoardKey{" +
                "key='" + key + '\'' +
                ", tag='" + tag + '\'' +
                ", nextSpot=" + nextSpot +
                '}';
    }
}
