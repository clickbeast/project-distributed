import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static String ROOT_PATH = "C:\\Users\\maes_\\Desktop\\school\\project-distributed-gitkraken\\MasterServer";
    public static boolean ON_WINDOWS = true;

    public static void main(String[] args) throws IOException {
        Registry registry = LocateRegistry.createRegistry(9000);
        registry.rebind("SlaveToMasterCommunication", new MasterServer());
        int aantalServers = 3;
        int aantalMailboxenPerServer = 100;
        //beginnen bij dit poortnummer en telkens incrementeren
        int startPoortNummer = 9001;
    }
}
