import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;

public class BulletinBoard extends UnicastRemoteObject implements Chat {
   private String path;
   int amountOfMessages;

    public BulletinBoard() throws RemoteException {
        path = System.getProperty("user.dir") + File.separator + "board.db";
        createDatabase();
        initializeMessageTable();
        amountOfMessages=0;
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
            System.err.println("[SLAVE] " + e.getMessage());
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
}
