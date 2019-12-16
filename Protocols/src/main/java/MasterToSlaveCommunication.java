import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface MasterToSlaveCommunication extends Remote {
    boolean ping() throws RemoteException;
    void sendServerList(LinkedList<ServerEntry> entries) throws RemoteException;
}
