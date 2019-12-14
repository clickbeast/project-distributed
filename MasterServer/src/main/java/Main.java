import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static boolean ON_WINDOWS = false;
    public static int NUMBER_OF_MAILBOXES_PER_SLAVE = 100;

    public static void main(String[] args) throws IOException {
        MasterServer server = new MasterServer();

        Registry slaveToMaster = LocateRegistry.createRegistry(9000);
        slaveToMaster.rebind("SlaveToMasterCommunication", server);

        Registry clientToMaster = LocateRegistry.createRegistry(8999);
        clientToMaster.rebind("ClientToMasterCommunication", server);

        server.run();
    }
}
