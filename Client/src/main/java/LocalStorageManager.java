import exceptions.AccountAlreadyExistsException;
import model.Message;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//TODO: MAKE ALL FUNCTIONS NOT STATIC, now static for testing
public class LocalStorageManager {
    private String path;

    public LocalStorageManager(String databaseName) {
        this.path = System.getProperty("user.dir") + File.separator + databaseName;
    }

    /**
     * Creates a Database at the given path
     */
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
    public void initializeAccountsDatabase() {
        String url = "jdbc:sqlite:" + path;
        String sql = "CREATE TABLE IF NOT EXISTS accounts ( loginname VARCHAR NOT NULL,password VARCHAR NOT NULL,salt" +
                " VARCHAR DEFAULT '',PRIMARY KEY (loginname));";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Saves an account, password gets salted+hashed here
     *
     * @param loginname loginname of the user
     * @param pass      unedited password of the user
     * @param salt      salt for the password, can be empty string
     */
    public void addAccount(String loginname, String pass, String salt) throws AccountAlreadyExistsException {
        if (salt == null) {
            salt = "";
        }
        initializeAccountsDatabase();

        String url = "jdbc:sqlite:" + path;
        String sql = "INSERT INTO accounts (loginname,password,salt) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loginname);
            pstmt.setString(2, bytesToHex(hash(pass + salt)));
            pstmt.setString(3, salt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new AccountAlreadyExistsException("Account " + loginname + " exists already.");
            }
            System.err.print(e.getErrorCode() + "\t");
            System.err.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * checks if the login with the given password is correct
     *
     * @param loginname loginname
     * @param pass      password
     * @return true if good login, false if bad login
     */
    public boolean login(String loginname, String pass) {
        String url = "jdbc:sqlite:" + path;

        String sql = "SELECT loginname,password,salt FROM accounts WHERE loginname = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loginname);
            ResultSet rs = pstmt.executeQuery();

            // There should only be one person with that loginname, but just to be sure.
            while (rs.next()) {

                if (rs.getString("password").equals(bytesToHex(hash(pass + rs.getString("salt"))))) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
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

    public List<Message> getMessagesFromUserID(int userId) {
        List<Message> messages = new ArrayList<>();
        String url = "jdbc:sqlite:" + path;

        String sql = "SELECT userID,text,messageDate FROM messages WHERE userID = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // There should only be one person with that loginname, but just to be sure.
            while (rs.next()) {
                messages.add(new Message(rs.getString("text"), rs.getInt("userID"),rs.getInt("fromUser")==1, rs.getLong("messageDate")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return messages;

    }

    public void storeMessage(int userId, String message, long timeMillis, boolean fromUser) {

        String url = "jdbc:sqlite:" + path;
        String sql = "INSERT INTO messages (userID,text,fromUser,messageDate) VALUES(?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, message);
            pstmt.setInt(3, fromUser?1:0);
            pstmt.setLong(4, timeMillis);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.print(e.getErrorCode() + "\t");
            System.err.println(e.getMessage());
        }

    }

    public void initializeConversationsDatabase() {
        String url = "jdbc:sqlite:" + path;
        String sql = "CREATE TABLE IF NOT EXISTS`conversations` (\n" +
                "\t`userId`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`contactname`\tTEXT,\n" +
                "\t`encryptKey`\tTEXT,\n" +
                "\t`nextSpot`\tINTEGER\n" +
                "\t`encryptKeyUs`\tTEXT,\n" +
                "\t`nextSpotUs`\tINTEGER\n" +
                ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS `messages` (\n" +
                    "\t`messageID`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`userID`\tINTEGER,\n" +
                    "\t`text`\tTEXT,\n" +
                    "\t`fromUser`\tINTEGER,\n" +
                    "\t`messageDate`\tTIMESTAMP  \n" +
                    ",FOREIGN KEY " +
                    "(userID) REFERENCES conversations(userID));";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + " " + e.getMessage());
        }
    }


}
