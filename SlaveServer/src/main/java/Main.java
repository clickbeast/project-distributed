import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static Registry registry;

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registryToServer = LocateRegistry.getRegistry("localhost", 9000);
        SlaveToMasterCommunication toServer = (SlaveToMasterCommunication) registryToServer.lookup("SlaveToMasterCommunication");

        int portNumber = toServer.getPort();

        Registry registryFromServer = LocateRegistry.createRegistry(portNumber);
        registryFromServer.rebind("MasterToSlaveCommunication", new SlaveServer(portNumber, toServer));
        System.out.println("Slave server created on port " + portNumber);
    }
}
