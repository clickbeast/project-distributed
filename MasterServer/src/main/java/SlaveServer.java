import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SlaveServer {
    private MasterToSlaveCommunication toSlave;
    private int portNumber;

    public SlaveServer(int portNumber, String ip) throws RemoteException, NotBoundException {
        this.portNumber = portNumber;
        System.out.println("[MASTER] Trying to make RMI connection to " + ip + ":" + portNumber);
        Registry registry = LocateRegistry.getRegistry(ip, portNumber);
        this.toSlave = (MasterToSlaveCommunication) registry.lookup("MasterToSlaveCommunication");
        System.out.println("[MASTER] New slave connected on port " + portNumber);
        if(this.toSlave.ping())
            System.out.println("[MASTER] ping succesfull.");
        else
            System.out.println("[MASTER] ping failed.");

    }

    public MasterToSlaveCommunication communicate(){
        return this.toSlave;
    }
}
