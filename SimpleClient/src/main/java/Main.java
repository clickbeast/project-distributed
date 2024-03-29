import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static Chat chat;

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException,
            NoSuchAlgorithmException {
        Registry registry = LocateRegistry.getRegistry("localhost", 9002);
        chat = (Chat) registry.lookup("Chat");

        System.out.println(chat.getMessage(0,"b4d465d59b2dd7510d9d6420c86f85d2"));
    }
    /**
     * hashes given string
     *
     * @param string string
     * @return hash of given string
     * @throws NoSuchAlgorithmException
     */
    private static byte[] hash(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] passwordByte = string.getBytes();
        return digest.digest(passwordByte);

    }

    /**
     * converts bytearray to string
     *
     * @param hash bytearray
     * @return
     */
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
