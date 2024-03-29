import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public interface Ping extends Remote {
    boolean ping() throws RemoteException;
    void kill() throws RemoteException;
    LinkedList<ServerEntry> getEntries() throws RemoteException;
}
