import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BulletinBoard extends UnicastRemoteObject implements Chat {
    /**
     * Iedere cel in het messageBoard houdt een set (hier een
     * HashMap) bij van entries, elke entry bestaat uit een
     * tag (waarmee de ontvanger het bericht kan vinden) en
     * een value (deze value bevat het bericht en de index+tag
     * van het volgende bericht).
     * De key in deze HashMaps is de tag en de waarde is de value.
     */
    private HashMap<String, String>[] messageBoard;

    /**
     * Ieder mailbox mag door meerdere threads gelezen worden
     * en mag door maximum 1 thread gewrite worden; Daarom
     * houden we een ReadWriteLock bij per cell in de mailbox
     */
    private ReadWriteLock[] readWriteLocks;


    private String path;

    public BulletinBoard() throws RemoteException {
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
            return false;
        }

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


                return rs.getString("message");
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }


    //TODO: implement
    public static String globalHashFunction(String s) {
        return null;
    }
}
