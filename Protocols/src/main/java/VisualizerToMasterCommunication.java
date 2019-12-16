import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface VisualizerToMasterCommunication extends Remote {
    int getLimit() throws RemoteException;
    LinkedList<ServerEntry> getSlaveList() throws RemoteException;
}
