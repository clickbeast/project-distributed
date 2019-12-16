import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

import javax.sound.sampled.FloatControl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static String PATH_TO_SLAVE_JAR = "/home/andres/Documents/project-distributed/MasterServer/SlaveServer";
    public static PrintWriter writer;

    static {
        try {
            writer = new PrintWriter("/home/andres/Desktop/MasterLOG.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static int NUMBER_OF_MAILBOXES_PER_SLAVE = 100;
    public static int NUMBER_OF_SLAVES = 10;
    public static Ping CONNECTION_TO_WATCHER;

    public static final String IP_OF_WATCHER = "localhost";
    public static final int PORT_OF_WATCHER = 7000;
    public static final int PORT_FOR_SLAVE_TO_MASTER_COMMUNICATION = 9000;
    public static final int PORT_FOR_CLIENT_TO_MASTER_COMMUNICATION = 8999;
    public static final int PORT_FOR_PINGING_TO_MASTER = 8998;

    public static void main(String[] args) throws IOException, NotBoundException {
        MasterServer server = new MasterServer();

        Registry slaveToMaster = LocateRegistry.createRegistry(PORT_FOR_SLAVE_TO_MASTER_COMMUNICATION);
        slaveToMaster.rebind("SlaveToMasterCommunication", server);

        Registry clientToMaster = LocateRegistry.createRegistry(PORT_FOR_CLIENT_TO_MASTER_COMMUNICATION);
        clientToMaster.rebind("ClientToMasterCommunication", server);

        Registry ping = LocateRegistry.createRegistry(PORT_FOR_PINGING_TO_MASTER);
        ping.rebind("Ping", server);

        Registry pingToWatcher = LocateRegistry.getRegistry(IP_OF_WATCHER, PORT_OF_WATCHER);
        CONNECTION_TO_WATCHER = (Ping) pingToWatcher.lookup("Ping");

        if (args.length == 0)
            server.run(NUMBER_OF_SLAVES);
    }

    public static void print(String string){
        System.out.println(string);
        LOG(string);
    }

    public static void printError(String string){
        System.err.println(string);
        LOG(string);
    }

    public static void LOG(String string) {
        writer.println(string);
        writer.flush();
    }
}