import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class Main {
    public static PrintWriter writer;
    public static boolean LOG = true;

    public static Registry REGISTRY;
    public static String IP = "localhost";
    public static int PORT_FOR_PINGING;
    public static int PORT_NUMBER;
    public static int NUMBER_OF_MAILBOXES;
    public static int BASE_MAILBOX;
    public static LinkedList<ServerEntry> entries;

    public static void main(String[] args) {
        try {
            NUMBER_OF_MAILBOXES = Integer.parseInt(args[0]);
            BASE_MAILBOX = Integer.parseInt(args[1]);

            Registry registryToServer = LocateRegistry.getRegistry(IP, 9000);
            SlaveToMasterCommunication toMaster = (SlaveToMasterCommunication) registryToServer.lookup("SlaveToMasterCommunication");

            PORT_NUMBER = toMaster.getPort();
            PORT_FOR_PINGING = PORT_NUMBER + 2000;


            try {
                writer = new PrintWriter("/home/adegeter/Desktop/LOG FILES/Slave_" + PORT_NUMBER + "_LOG.txt", "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            SlaveServer slaveServer = new SlaveServer(PORT_NUMBER, toMaster);

            Registry registryFromServer = LocateRegistry.createRegistry(PORT_NUMBER);
            registryFromServer.rebind("MasterToSlaveCommunication", slaveServer);

            Registry pingRegistry = LocateRegistry.createRegistry(PORT_FOR_PINGING);
            pingRegistry.rebind("Ping", slaveServer);

            entries = new LinkedList<>();

            synchronized (entries) {
                entries = toMaster.confirmConfiguration(
                        PORT_NUMBER,
                        InetAddress.getLocalHost().toString(),
                        BASE_MAILBOX,
                        BASE_MAILBOX + NUMBER_OF_MAILBOXES - 1);

                printMailboxes();
            }

            MasterListener masterListener = new MasterListener();
            Thread thread = new Thread(masterListener);
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printMailboxes() {
        Main.print("[SLAVE: " + PORT_NUMBER + "] ============= ENTRIES =============");
        for (ServerEntry entry : entries) {
            Main.print("[SLAVE: " + PORT_NUMBER + "] [" + entry.getStartMailbox() + "," + entry.getEndMailbox() + "] on " + entry.getIp() + ":" + entry.getPortNumber());
        }
        Main.print("[SLAVE: " + PORT_NUMBER + "] ===================================");
    }

    public static void print(String string) {
        System.out.println(string);
        LOG(string);
    }

    public static void printError(String string) {
        System.out.println(string);
        LOG(string);
    }

    public static void LOG(String string) {
        synchronized (writer) {
            if (LOG) {
                writer.println(string);
            }
        }
    }
}