import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static Registry REGISTRY;
    public static String IP = "localhost";

    public static void main(String[] args) throws RemoteException, NotBoundException, UnknownHostException {
        Registry registryToServer = LocateRegistry.getRegistry(IP, 9000);
        SlaveToMasterCommunication toServer = (SlaveToMasterCommunication) registryToServer.lookup("SlaveToMasterCommunication");

        int portNumber = toServer.getPort();

        Registry registryFromServer = LocateRegistry.createRegistry(portNumber);
        registryFromServer.rebind("MasterToSlaveCommunication", new SlaveServer(portNumber, toServer));
        toServer.confirmConfiguration(portNumber, InetAddress.getLocalHost().toString());
    }
}