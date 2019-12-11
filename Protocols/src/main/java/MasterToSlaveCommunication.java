import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MasterToSlaveCommunication extends Remote {
    boolean ping() throws RemoteException;
}
