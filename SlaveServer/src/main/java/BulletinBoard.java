import java.io.File;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BulletinBoard extends UnicastRemoteObject implements Chat, VisualizerToSlaveCommunication {
    private String path;
    int amountOfMessages;

    public BulletinBoard(boolean backup, int portNumber) throws RemoteException {
        path = System.getProperty("user.dir") + File.separator + "board" + portNumber + (backup ? "bak" : "") + ".db";
        createDatabase();
        initializeMessageTable();
        amountOfMessages = 0;
    }

    public void createDatabase() {
        String url = "jdbc:sqlite:" + path;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }

        } catch (SQLException e) {
            System.err.println("[SLAVE] " + e.getMessage());
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
            System.err.println("[SLAVE] " + e.toString());
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
            System.err.print("[SLAVE] " + e.getErrorCode() + "\t");
            System.err.println("[SLAVE] " + e.getMessage());
            return false;
        }

        amountOfMessages++;
        return true;
    }

    public String getMessage(int boxNumber, String tag) {
        String url = "jdbc:sqlite:" + path;
        try {
            tag = new String(hash(tag), StandardCharsets.ISO_8859_1);
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

                amountOfMessages--;
                return rs.getString("message");
            }

        } catch (SQLException e) {
            System.err.println("[SLAVE] " + e.getMessage());
        }
        return null;
    }

    public List<MailBoxEntry> getAllMailBoxEntries() {
        String url = "jdbc:sqlite:" + path;
        List<MailBoxEntry> mailBoxEntries = new ArrayList<>();
        String sql = "SELECT boxNumber,tag,message FROM messages /*WHERE boxNumber BETWEEN ? AND ?*/";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            // There should only be one person with that loginname, but just to be sure.
            while (rs.next()) {
                mailBoxEntries.add(new MailBoxEntry(rs.getInt("boxNumber"), rs.getString("tag"), rs.getString(
                        "message")));
            }

        } catch (SQLException e) {
            System.err.println("[SLAVE] " + e.getMessage());
        }
        return mailBoxEntries;
    }

    @Override
    public String getServerWithMailbox(int boxnumber) throws RemoteException {
        synchronized (Main.entries) {
            for (ServerEntry entry : Main.entries) {
                if (entry.contains(boxnumber))
                    return entry.chatAddress();
            }
        }
        return null;
    }

    @Override
    public void kill() throws RemoteException {
        System.exit(0);
    }

    @Override
    public int getLimit() throws RemoteException {
        int maxMailbox = 0;

        synchronized (Main.entries) {
            for (ServerEntry entry : Main.entries) {
                if (entry.getEndMailbox() > maxMailbox)
                    maxMailbox = entry.getEndMailbox();
            }
        }
        return maxMailbox;
    }

    private static byte[] hash(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] passwordByte = string.getBytes(StandardCharsets.ISO_8859_1);
        return digest.digest(passwordByte);

    }

}
