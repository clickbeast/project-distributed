import exceptions.AccountAlreadyExistsException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BoardKey;
import model.Conversation;
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
import java.util.Random;

@SuppressWarnings("Duplicates")
public class LocalStorageManager {
    private String path;

    public LocalStorageManager(String filepath) {
        this.path = /*System.getProperty("User.dir") + File.separator + */filepath;
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
            System.out.println("what");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates an database for accounts to be saved at, including a login, pass and salt.
     * This won't do anything if the database is already initialized
     */
    public void initializeAccountsDatabase() {
        String url = "jdbc:sqlite:" + path;
        String sql = "CREATE TABLE IF NOT EXISTS accounts ( userId PRIMARY KEY AUTOINCREMENT, loginname VARCHAR NOT " +
                "NULL,password VARCHAR NOT NULL,salt" +
                " VARCHAR DEFAULT '')";
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
     * @param loginname loginname of the User
     * @param pass      unedited password of the User
     * @param salt      salt for the password, can be empty string
     */
    public int addAccount(String loginname, String pass, String salt) throws AccountAlreadyExistsException {
        if (salt == null) {
            salt = "";
        }
        if (salt.equals("")) {
            String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
            Random rnd = new Random();

            StringBuilder sb2 = new StringBuilder(32);
            for (int i = 0; i < 32; i++) {
                sb2.append(AB.charAt(rnd.nextInt(AB.length())));
            }
            salt = sb2.toString();
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
            sql = "SELECT last_insert_rowid()";
            ResultSet rs = conn.prepareStatement(sql).executeQuery();

            return rs.getInt("last_insert_rowid()");
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new AccountAlreadyExistsException("Account " + loginname + " exists already.");
            }
            System.err.print(e.getErrorCode() + "\t");
            System.err.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        return -1;
    }

    /**
     * checks if the login with the given password is correct
     *
     * @param loginname loginname
     * @param pass      password
     * @return true if good login, false if bad login
     */
    public int login(String loginname, String pass) {
        String url = "jdbc:sqlite:" + path;

        String sql = "SELECT userId,loginname,password,salt FROM accounts WHERE loginname = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loginname);
            ResultSet rs = pstmt.executeQuery();

            // There should only be one person with that loginname, but just to be sure.
            while (rs.next()) {

                if (rs.getString("password").equals(bytesToHex(hash(pass + rs.getString("salt"))))) {
                    return rs.getInt("userId");
                }
            }

            return -1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return -1;
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
        List<Message> messages = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:" + path;

        String sql = "SELECT userID,text,messageDate FROM messages WHERE userID = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // There should only be one person with that loginname, but just to be sure.
            while (rs.next()) {
                messages.add(new Message(rs.getString("text"),
                        rs.getInt("userID"),
                        rs.getLong("messageDate"),
                        rs.getInt("fromUser") == 1,
                        rs.getInt("delivered") == 1,
                        rs.getInt("read") == 1));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return messages;

    }

    public int storeMessage(Message message) {

        String url = "jdbc:sqlite:" + path;
        String sql = "INSERT INTO messages (contactId,text,fromUser,seen,delivered,messageDate) VALUES(?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, message.getContactId());
            pstmt.setString(2, message.getText());
            pstmt.setInt(3, message.isFromUser() ? 1 : 0);
            pstmt.setInt(3, message.isSeen() ? 1 : 0);
            pstmt.setInt(3, message.isDelivered() ? 1 : 0);
            pstmt.setLong(4, message.getTimeStamp());
            pstmt.executeUpdate();
            sql = "SELECT last_insert_rowid()";
            ResultSet rs = conn.prepareStatement(sql).executeQuery();

            return rs.getInt("last_insert_rowid()");
        } catch (SQLException e) {
            System.err.print(e.getErrorCode() + "\t");
            System.err.println(e.getMessage());
        }
        return -1;

    }

    public List<Conversation> getConversations(int userId) {
        List<Conversation> conversations = new ArrayList<>();
        String url = "jdbc:sqlite:" + path;

        String sql = "SELECT contactname,encryptKey,tag,nextSpot,encryptKeyUs,tagUs,nextSpotUs FROM conversations " +
                "WHERE userID = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // There should only be one person with that loginname, but just to be sure.
            while (rs.next()) {
                conversations.add(new Conversation(rs.getInt("contactId"),
                        userId,
                        rs.getString("contactname"),
                        new BoardKey(
                                rs.getString("encryptKey"),
                                rs.getString("tag"),
                                rs.getInt("nextSpot")),
                        new BoardKey(
                                rs.getString("encryptKeyUs"),
                                rs.getString("tagUs"),
                                rs.getInt("nextSpotUs")),
                        (ObservableList<Message>) getMessagesFromUserID(userId)));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return conversations;
    }

    public int saveConversation(Conversation conversation) {
        String url = "jdbc:sqlite:" + path;
        String sql = "INSERT INTO conversations (contactname,encryptKey,tag,nextSpot,encryptKeyUs,tagUs,nextSpotUs) " +
                "VALUES(?,?,?,?,?,?,?); ";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, conversation.getUserName());
            pstmt.setString(2, conversation.getBoardKey().getKey());
            pstmt.setString(3, conversation.getBoardKey().getTag());
            pstmt.setInt(4, conversation.getBoardKey().getNextSpot());
            pstmt.setString(5, conversation.getBoardKeyUs().getKey());
            pstmt.setString(6, conversation.getBoardKeyUs().getTag());
            pstmt.setInt(7, conversation.getBoardKeyUs().getNextSpot());
            pstmt.executeUpdate();
            sql = "SELECT last_insert_rowid()";
            ResultSet rs = conn.prepareStatement(sql).executeQuery();

            return rs.getInt("last_insert_rowid()");
        } catch (SQLException e) {
            System.err.print(e.getErrorCode() + "\t");
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public void initializeConversationsDatabase() {
        String url = "jdbc:sqlite:" + path;
        String sql = "CREATE TABLE IF NOT EXISTS`conversations` (\n" +
                "\t`contactId`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`userId`\tINTEGER,\n" +
                "\t`contactname`\tTEXT,\n" +
                "\t`encryptKey`\tTEXT,\n" +
                "\t`tag`\tTEXT,\n" +
                "\t`nextSpot`\tINTEGER,\n" +
                "\t`encryptKeyUs`\tTEXT,\n" +
                "\t`tagUs`\tTEXT,\n" +
                "\t`nextSpotUs`\tINTEGER\n" +
                ",FOREIGN KEY " +
                "(userId) REFERENCES accounts(userId));";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS `messages` (\n" +
                    "\t`messageID`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`contactId`\tINTEGER,\n" +
                    "\t`text`\tTEXT,\n" +
                    "\t`fromUser`\tINTEGER,\n" +
                    "\t`seen`\tINTEGER,\n" +
                    "\t`delivered`\tINTEGER,\n" +
                    "\t`messageDate`\tTIMESTAMP  \n" +
                    ",FOREIGN KEY " +
                    "(contactId) REFERENCES conversations(contactId));";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + " " + e.getMessage());
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void deleteConversation(Conversation conversation) {
        String url = "jdbc:sqlite:" + path;
        String sql = "DELETE FROM conversations  WHERE contactId = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, conversation.getContactId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.print(e.getErrorCode() + "\t");
            System.err.println(e.getMessage());
        }
    }

    public void updateConversation(String text, int contactId) {
        String url = "jdbc:sqlite:" + path;
        String sql = "UPDATE conversations SET contactname = ? WHERE contactId = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, text);
            pstmt.setInt(2, contactId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.print(e.getErrorCode() + "\t");
            System.err.println(e.getMessage());
        }
    }
}
