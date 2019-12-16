import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class Main {
    public static Registry REGISTRY;
    public static String IP = "localhost";
    public static int PORT_NUMBER;
    public static int NUMBER_OF_MAILBOXES;
    public static int BASE_MAILBOX;
    public static LinkedList<ServerEntry> entries;

    public static void main(String[] args) throws RemoteException, NotBoundException, UnknownHostException {
        NUMBER_OF_MAILBOXES = Integer.parseInt(args[0]);
        BASE_MAILBOX = Integer.parseInt(args[1]);

        Registry registryToServer = LocateRegistry.getRegistry(IP, 9000);
        SlaveToMasterCommunication toMaster = (SlaveToMasterCommunication) registryToServer.lookup("SlaveToMasterCommunication");

        PORT_NUMBER = toMaster.getPort();

        Registry registryFromServer = LocateRegistry.createRegistry(PORT_NUMBER);
        registryFromServer.rebind("MasterToSlaveCommunication", new SlaveServer(PORT_NUMBER, toMaster));

        synchronized (entries) {
            entries = toMaster.confirmConfiguration(
                    PORT_NUMBER,
                    InetAddress.getLocalHost().toString(),
                    BASE_MAILBOX,
                    BASE_MAILBOX + NUMBER_OF_MAILBOXES - 1);

            Main.print("[SLAVE: " + PORT_NUMBER + "] ============= ENTRIES =============");
            for (ServerEntry entry : entries) {
                Main.print("[SLAVE: " + PORT_NUMBER + "] [" + entry.getStartMailbox() + "," + entry.getEndMailbox() + "] on " + entry.getIp() + ":" + entry.getPortNumber());
            }
            Main.print("[SLAVE: " + PORT_NUMBER + "] ===================================");
        }

        MasterListener masterListener = new MasterListener();
        masterListener.run();
    }

    public static void print(String string){
        System.out.println(string);
    }

    public static void printError(String string){
        System.err.println(string);
    }
}