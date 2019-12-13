import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BulletinBoard extends UnicastRemoteObject implements Chat {


    private String path;

    public BulletinBoard() throws RemoteException {
        super();
        path = System.getProperty("user.dir") + File.separator + "board.db";
        createDatabase();
        initializeMessageTable();

    }

    public void createDatabase() {
        String url = "jdbc:sqlite:" + path;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates an database for accounts to be saved at, including a login, pass and salt.
     * This won't do anything if the database is already initialized
     */
    public void initializeMessageTable() {
        String url = "jdbc:sqlite:" + path;

        String sql = "CREATE TABLE IF NOT EXISTS messages ( boxNumber INT NOT NULL,tag VARCHAR NOT NULL,message " +
                "VARCHAR NOT NULL,PRIMARY KEY (boxNumber,tag));";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public boolean sendMessage(int boxNumber, String tag, String message) {
        String url = "jdbc:sqlite:" + path;
        String sql = "INSERT INTO messages (boxNumber,tag,message) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, boxNumber);
            pstmt.setString(2, tag);
            pstmt.setString(3, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.print(e.getErrorCode() + "\t");
            System.err.println(e.getMessage());
        }

        return true;
    }

    public String getMessage(int boxNumber, String tag) {
        String url = "jdbc:sqlite:" + path;
        try {
            tag = bytesToHex(hash(tag));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String sql = "SELECT message FROM messages WHERE boxNumber = ? AND tag = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, boxNumber);
            pstmt.setString(2, tag);

            ResultSet rs = pstmt.executeQuery();

            // There should only be one person with that loginname, but just to be sure.
            if (rs.next()) {
                sql = "DELETE FROM messages WHERE boxNumber = ? AND tag = ?";

                PreparedStatement pstmt2 = conn.prepareStatement(sql);
                pstmt2.setInt(1, boxNumber);
                pstmt2.setString(2, tag);
                pstmt2.executeUpdate();


                return rs.getString("message");
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
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
