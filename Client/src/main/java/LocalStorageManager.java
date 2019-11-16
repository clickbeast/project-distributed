import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//TODO: MAKE ALL FUNCTIONS NOT STATIC, now static for testing
public class LocalStorageManager {
    /**
     * Creates a Database at the given path
     *
     * @param path the path for the database to be saved at
     */
    public static void createDatabase(String path) {
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
     *
     * @param path the path for the database to be initialzed
     */
    public static void initializeAccountsDatabase(String path) {
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
     * @param path      path of the database
     * @param loginname loginname of the user
     * @param pass      unedited password of the user
     * @param salt      salt for the password, can be empty string
     */
    public static void addAccount(String path, String loginname, String pass, String salt) {
        if (salt == null) {
            salt = "";
        }
        initializeAccountsDatabase(path);

        String url = "jdbc:sqlite:" + path;
        String sql = "INSERT INTO accounts (loginname,password,salt) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loginname);
            pstmt.setString(2, bytesToHex(hash(pass + salt)));
            pstmt.setString(3, salt);
            pstmt.executeUpdate();
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }

    }

    public static boolean login(String path, String loginname, String pass) {
        String url = "jdbc:sqlite:" + path;

        String sql = "SELECT loginname,password,salt FROM accounts WHERE loginname = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, loginname);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                if (rs.getString("password").equals(bytesToHex(hash(pass + rs.getString("salt"))))) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static byte[] hash(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] passwordByte = string.getBytes();
        return digest.digest(passwordByte);

    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {

        Parameters params = new Parameters(args);
        if (params.getNamed().containsKey("dbpath")) {
            createDatabase(params.getNamed().get("dbpathd"));
           // initializeAccountsDatabase(params.getNamed().get("dbpath"));
            //very secure login
            //addAccount(params.getNamed().get("dbpath"), "admin", "admin", "salt");
           /* if (login(params.getNamed().get("dbpath"), "admin", "admin")) {
                System.out.println("good login");
            } else {
                System.out.println("bad login");
            }
            if (login(params.getNamed().get("dbpath"), "admin", "admin2")) {
                System.out.println("good login");
            } else {
                System.out.println("bad login");
            }*/
        }
    }


    /*    public static void main(String[] args) throws NoSuchAlgorithmException {
        Person person1 = new Person("name1", "Stad1", "phone1");
        Person person1Fake = new Person("name0", "Stad1", "phone1");
        Person person2 = new Person("name2", "Stad2", "phone2");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] person1Byte = person1.getBytes();
        byte[] encodedhashp1 = digest.digest(person1Byte);
        System.out.println(bytesToHex(encodedhashp1));

        byte[] person1FakeByte = person1Fake.getBytes();
        byte[] person2Byte = person2.getBytes();
        byte[] encodedhashp1fake = digest.digest(person1FakeByte);
        byte[] encodedhashp2 = digest.digest(person2Byte);

        System.out.println(bytesToHex(encodedhashp1fake));
        System.out.println(bytesToHex(encodedhashp2));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }*/
}
