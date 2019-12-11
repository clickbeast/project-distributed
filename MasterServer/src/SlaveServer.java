import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SlaveServer {
    private MasterToSlaveCommunication toSlave;
    private int portNumber;

    public SlaveServer(int portNumber) throws RemoteException, NotBoundException {
        this.portNumber = portNumber;
        Registry registry = LocateRegistry.getRegistry("localhost", portNumber);
        this.toSlave = (MasterToSlaveCommunication) registry.lookup("MasterToSlaveCommunication");
        System.out.println("New slave connected on port " + portNumber);
    }

    public MasterToSlaveCommunication communicate(){
        return this.toSlave;
    }
}
